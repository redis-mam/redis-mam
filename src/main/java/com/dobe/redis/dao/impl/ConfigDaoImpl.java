package com.dobe.redis.dao.impl;

import com.dobe.redis.dao.ConfigDao;
import com.dobe.redis.model.*;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import static com.dobe.redis.model.RedisContainer.*;

/**
 *
 * @author zc.ding
 * @since 1.0
 */
@Component
public class ConfigDaoImpl implements ConfigDao, InitializingBean {

    @Value("${config.dir.path:}")
    protected String configDirPath;

    @Override
    public ResponseEntity<?> addUser(User user) {
        if (USERS.stream().anyMatch(o -> o.getName().equals(user.getName()))) {
            return ResponseEntity.error("已经存在用户：" + user.getName());
        }
        if (USERS.add(user)) {
            this.saveXml();
            return ResponseEntity.SUCCESS;
        }
        return ResponseEntity.ERROR;
    }

    @Override
    public ResponseEntity<?> delUser(String name) {
        if (USER_ROLE_REDIS_INFOS.stream().anyMatch(o -> o.getUserName().equals(name))) {
            return ResponseEntity.error("服务依赖此属性：" + name);
        }
        User delObj = USERS.stream().filter(o -> o.getName().equals(name)).findFirst().orElseGet(User::new);
        if (USERS.remove(delObj)) {
            this.saveXml();
            return ResponseEntity.SUCCESS;
        }
        return ResponseEntity.ERROR;
    }

    @Override
    public ResponseEntity<?> updateUserState(String name, String state) {
        User user = USERS.stream().filter(o -> o.getName().equals(name)).findFirst().orElseGet(User::new);
        if (name.equals(user.getName())) {
            user.setState(state);
            this.saveXml();
            return ResponseEntity.SUCCESS;
        }
        return ResponseEntity.ERROR;
    }

    @Override
    public List<User> findUserList(User user) {
        return USERS.stream().filter(o -> {
            if(user != null && StringUtils.isNotBlank(user.getName())){
                return o.getName().toUpperCase().contains(user.getName().toUpperCase().trim());
            }
            return true;
        }).collect(Collectors.toList());
    }

    @Override
    public User findUserByName(String name) {
        return USERS.stream().filter(o -> o.getName().equals(name)).findAny().orElse(null);
    }

    @Override
    public ResponseEntity<?> addRole(Role role) {
        if (ROLES.stream().anyMatch(o -> o.getName().equals(role.getName()))) {
            return ResponseEntity.error("已经存在角色：" + role.getName());
        }
        if (ROLES.add(role)) {
            this.saveXml();
            return ResponseEntity.SUCCESS;
        }
        return ResponseEntity.ERROR;
    }

    @Override
    public ResponseEntity<?> delRole(String name) {
        if (USER_ROLE_REDIS_INFOS.stream().anyMatch(o -> o.getRoleName().equals(name))) {
            return ResponseEntity.error("服务依赖此属性：" + name);
        }
        Role delObj = ROLES.stream().filter(o -> o.getName().equals(name)).findAny().orElseGet(Role::new);
        if (ROLES.remove(delObj)) {
            this.saveXml();
            return ResponseEntity.SUCCESS;
        }
        return ResponseEntity.ERROR;
    }

    @Override
    public List<Role> findRoleList(Role role) {
        return ROLES.stream().filter(o -> {
            if (StringUtils.isNotBlank(role.getName())) {
                return o.getName().toUpperCase().contains(role.getName().toUpperCase().trim());
            }
            return true;
        }).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> addRedisInfo(RedisInfo redisInfo) {
        if (REDIS_INFOS.stream().anyMatch(o -> o.getName().equals(redisInfo.getName()))) {
            return ResponseEntity.error("已经存在redis配置：" + redisInfo.getName());
        }
        if (REDIS_INFOS.add(redisInfo)) {
            this.saveXml();
            return ResponseEntity.SUCCESS;
        }
        return ResponseEntity.ERROR;
    }

    @Override
    public ResponseEntity<?> delRedisInfo(String name) {
        if (USER_ROLE_REDIS_INFOS.stream().anyMatch(o -> o.getRedisInfoName().equals(name))) {
            return ResponseEntity.error("服务依赖此属性：" + name);
        }
        RedisInfo delObj = REDIS_INFOS.stream().filter(o -> o.getName().equals(name)).findAny().orElseGet(RedisInfo::new);
        if (REDIS_INFOS.remove(delObj)) {
            this.saveXml();
            return ResponseEntity.success(delObj);
        }
        return ResponseEntity.ERROR;
    }

    @Override
    public List<RedisInfo> findRedisInfoList(RedisInfo redisInfo) {
        return REDIS_INFOS.stream().filter(o -> {
            if (StringUtils.isNotBlank(redisInfo.getName())) {
                return o.getName().toUpperCase().contains(redisInfo.getName().toUpperCase().trim());
            }
            return true;
        }).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> addUserRoleRedisInfo(UserRoleRedisInfo userRoleRedisInfo) {
        if(USERS.stream().noneMatch(o -> o.getName().equals(userRoleRedisInfo.getUserName()))){
            return ResponseEntity.error("未找到对应用户：" + userRoleRedisInfo.getUserName());
        }
        if(ROLES.stream().noneMatch(o -> o.getName().equals(userRoleRedisInfo.getRoleName()))){
            return ResponseEntity.error("未找到对应角色：" + userRoleRedisInfo.getRoleName());
        }
        if(REDIS_INFOS.stream().noneMatch(o -> o.getName().equals(userRoleRedisInfo.getRedisInfoName()))){
            return ResponseEntity.error("未找到对应Redis配置：" + userRoleRedisInfo.getRedisInfoName());
        }
        if (USER_ROLE_REDIS_INFOS.stream().anyMatch(o -> o.getUserName().equals(userRoleRedisInfo.getUserName())
                && o.getRoleName().equals(userRoleRedisInfo.getRoleName())
                && o.getRedisInfoName().equals(userRoleRedisInfo.getRedisInfoName()))) {
            return ResponseEntity.error("此类配置已经存在：" + userRoleRedisInfo.getRedisInfoName());
        }
        userRoleRedisInfo.setName(UUID.randomUUID().toString());
        if (USER_ROLE_REDIS_INFOS.add(userRoleRedisInfo)) {
            this.saveXml();
            return ResponseEntity.SUCCESS;
        }
        return ResponseEntity.ERROR;
    }

    @Override
    public boolean delUserRoleRedisInfo(String name) {
        UserRoleRedisInfo delObj =
                USER_ROLE_REDIS_INFOS.stream().filter(o -> o.getName().equals(name)).findAny().orElseGet(UserRoleRedisInfo::new);
        boolean flag = USER_ROLE_REDIS_INFOS.remove(delObj);
        if (flag) {
            this.saveXml();
        }
        return flag;
    }

    @Override
    public List<UserRoleRedisInfo> findUserRoleRedisInfoList(UserRoleRedisInfo userRoleRedisInfo) {
        return USER_ROLE_REDIS_INFOS.stream().filter(o -> {
            if (StringUtils.isNotBlank(userRoleRedisInfo.getName())) {
                return o.getName().toUpperCase().contains(userRoleRedisInfo.getName().toUpperCase().trim());
            }
            return true;
        }).collect(Collectors.toList());
    }

//    private synchronized void loadXml() throws Exception {
//        SAXBuilder sbBuilder = new SAXBuilder();
//        // 找到Document
//        Document document = sbBuilder.build(configDirPath);
//        // 读取根元素
//        Element root = document.getRootElement();
//        // 加载users
//        USERS.addAll(parseUser(root.getChild("users").getChildren("user")));
//        // 加载roles
//        ROLES.addAll(parseRole(root.getChild("roles").getChildren("role")));
//        // 加载redis配置信息
//        REDIS_INFOS.addAll(parseRedisInfo(root.getChild("redisInfos").getChildren("redisInfo")));
//        // 加载userRoleRedisInfo
//        USER_ROLE_REDIS_INFOS.addAll(parseUserRoleRedisInfo(root.getChild("userRoleRedisInfos").getChildren("userRoleRedisInfo")));
//    }

    /**
    *  获得xml中根元素节点
    *  @param fileName xml文件名称
    *  @return org.jdom.Element
    *  @author                  ：zc.ding@foxmail.com
    */
    Element getDocumentRoot(String fileName) throws Exception{
        if (StringUtils.isBlank(configDirPath)) {
            configDirPath = new ClassPathResource("/conf").getFile().getAbsolutePath();
        }
        return new SAXBuilder().build(configDirPath + File.separator + fileName).getRootElement();
    }

    private synchronized void saveXml() {
        //声明一个Document对象
        Document document = new Document();
        Element root = new Element("config");
        //定义根节点
        Element users = new Element("users");
        USERS.forEach(obj -> users.addContent(createUserElement(obj)));
        Element roles = new Element("roles");
        ROLES.forEach(obj -> roles.addContent(createRoleElement(obj)));
        Element redisInfos = new Element("redisInfos");
        REDIS_INFOS.forEach(obj -> redisInfos.addContent(createRedisInfoElement(obj)));
        Element userRoleRedisInfos = new Element("userRoleRedisInfos");
        USER_ROLE_REDIS_INFOS.forEach(obj -> userRoleRedisInfos.addContent(createUserRoleRedisInfoElement(obj)));

        root.addContent(users);
        root.addContent(roles);
        root.addContent(redisInfos);
        root.addContent(userRoleRedisInfos);
        document.addContent(root);

        //用来输出XML文件
        XMLOutputter out = new XMLOutputter();
        //设置输出编码
        out.setFormat(out.getFormat().setEncoding("UTF-8"));
        try{
            //输出XML文件
            out.output(document, new FileOutputStream(configDirPath));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private Element createUserElement(User user) {
        Element element = new Element("user");
        element.setAttribute("name", Optional.ofNullable(user.getName()).orElse(""));
        element.setAttribute("pwd", Optional.ofNullable(user.getPwd()).orElse(""));
        element.setAttribute("state", Optional.ofNullable(String.valueOf(user.getState())).orElse(""));
        element.setAttribute("description", Optional.ofNullable(user.getDescription()).orElse(""));
        return element;
    }

    List<User> parseUser(List<?> list) {
        return list.stream().map(o -> {
            Element e = (Element) o;
            User obj = new User();
            obj.setName(e.getAttribute("name").getValue());
            obj.setPwd(e.getAttribute("pwd").getValue());
            obj.setState(e.getAttribute("state").getValue());
            obj.setDescription(e.getAttribute("description").getValue());
            return obj;
        }).collect(Collectors.toList());
    }

    private Element createRoleElement(Role role) {
        Element element = new Element("role");
        element.setAttribute("name", role.getName());
        element.setAttribute("create", role.getCreate());
        element.setAttribute("delete", role.getDelete());
        element.setAttribute("read", role.getRead());
        element.setAttribute("includeKey", role.getIncludeKey());
        element.setAttribute("exclusiveKey", role.getExclusiveKey());
        element.setAttribute("description", role.getDescription());
        return element;
    }

    List<Role> parseRole(List<?> list) {
        return list.stream().map(o -> {
            Element e = (Element) o;
            Role obj = new Role();
            obj.setName(e.getAttribute("name").getValue());
            obj.setCreate(e.getAttribute("create").getValue());
            obj.setDelete(e.getAttribute("delete").getValue());
            obj.setRead(e.getAttribute("read").getValue());
            obj.setIncludeKey(e.getAttribute("includeKey").getValue());
            obj.setExclusiveKey(e.getAttribute("exclusiveKey").getValue());
            obj.setDescription(e.getAttribute("description").getValue());
            return obj;
        }).collect(Collectors.toList());
    }

    private Element createRedisInfoElement(RedisInfo redisInfo) {
        Element element = new Element("redisInfo");
        element.setAttribute("name", redisInfo.getName());
        element.setAttribute("nodes", redisInfo.getNodes());
        element.setAttribute("description", redisInfo.getDescription());
        return element;
    }

    List<RedisInfo> parseRedisInfo(List<?> list) {
        return list.stream().map(o -> {
            Element e = (Element) o;
            RedisInfo obj = new RedisInfo();
            obj.setName(e.getAttribute("name").getValue());
            obj.setNodes(e.getAttribute("nodes").getValue());
            obj.setDescription(e.getAttribute("description").getValue());
            obj.setPwd(e.getAttribute("pwd").getValue());
            obj.setType(e.getAttribute("type").getValue());
            obj.parseNodes();
            RedisContainer.REDIS_ADD_QUEUE.add(obj);
            System.out.println("Are you ok?");
            return obj;
        }).collect(Collectors.toList());
    }

    private Element createUserRoleRedisInfoElement(UserRoleRedisInfo userRoleRedisInfo) {
        Element element = new Element("userRoleRedisInfo");
        element.setAttribute("name", userRoleRedisInfo.getName());
        element.setAttribute("userName", userRoleRedisInfo.getUserName());
        element.setAttribute("roleName", userRoleRedisInfo.getRoleName());
        element.setAttribute("redisInfoName", userRoleRedisInfo.getRedisInfoName());
        element.setAttribute("description", userRoleRedisInfo.getDescription());
        return element;
    }

    List<UserRoleRedisInfo> parseUserRoleRedisInfo(List<?> list) {
        return list.stream().map(o -> {
            Element e = (Element) o;
            UserRoleRedisInfo obj = new UserRoleRedisInfo();
            obj.setName(e.getAttribute("name").getValue());
            obj.setUserName(e.getAttribute("userName").getValue());
            obj.setRoleName(e.getAttribute("roleName").getValue());
            obj.setRedisInfoName(e.getAttribute("redisInfoName").getValue());
            obj.setDescription(e.getAttribute("description").getValue());
            return obj;
        }).collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        if (StringUtils.isBlank(configDirPath)) {
//            configDirPath = new ClassPathResource("/conf").getFile().getAbsolutePath();
//        }
//        loadXml();
    }
}
