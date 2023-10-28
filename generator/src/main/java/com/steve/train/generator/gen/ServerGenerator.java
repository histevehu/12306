package com.steve.train.generator.gen;

import cn.hutool.core.date.DateTime;
import com.steve.train.generator.util.DBUtil;
import com.steve.train.generator.util.Field;
import com.steve.train.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ServerGenerator {
    // 指定vue生成的页面是否只读，若为只读页面则只能查询数据，无法增删改
    static boolean readOnly = false;
    static String vuePath = "admin/src/views/main/";
    // 后端接口、服务、实体类生成路径
    static String serverPath = "[module]/src/main/java/com/steve/train/[module]/";
    static String pomPath = "generator/pom.xml";
    // 模块名
    static String module = "member";
    // static {
    //     new File(serverPath).mkdirs();
    // }

    public static void main(String[] args) throws Exception {
        // 获取mybatis-generator
        String generatorPath = getGeneratorPath();
        // 比如generator-config-member.xml，得到module = member
        module = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module: " + module);
        // 以模块名创建文件夹
        serverPath = serverPath.replace("[module]", module);
        new File(serverPath).mkdirs();
        System.out.println("servicePath: " + serverPath);

        // 读取table节点
        Document document = new SAXReader().read("generator/" + generatorPath);
        // "//"为从xml根节点寻找， pom是xml命名空间，configurationFile是节点名，如果要找属性，则是"@"
        Node table = document.selectSingleNode("//table");
        System.out.println(table);
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");
        System.out.println(tableName.getText() + "/" + domainObjectName.getText());

        // 为DBUtil设置数据源
        Node connectionURL = document.selectSingleNode("//@connectionURL");
        Node userId = document.selectSingleNode("//@userId");
        Node password = document.selectSingleNode("//@password");
        System.out.println("url: " + connectionURL.getText());
        System.out.println("user: " + userId.getText());
        System.out.println("password: " + password.getText());
        DBUtil.url = connectionURL.getText();
        DBUtil.user = userId.getText();
        DBUtil.password = password.getText();

        String now = DateTime.now().toString();
        // 示例：表名 train_member
        // Domain = Member
        String Domain = domainObjectName.getText();
        // domain = member
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        // do_main = train-member
        String do_main = tableName.getText().replaceAll("_", "-");
        // 表中文名
        String tableNameCn = DBUtil.getTableComment(tableName.getText());
        // 获取数据表字段信息
        List<Field> fieldList = DBUtil.getColumnByTableName(tableName.getText());
        Set<String> typeSet = getJavaTypes(fieldList);

        // 组装参数
        Map<String, Object> param = new HashMap<>();
        param.put("module", module);
        param.put("Domain", Domain);
        param.put("domain", domain);
        param.put("do_main", do_main);
        param.put("tableNameCn", tableNameCn);
        param.put("DateAndTime", now);
        param.put("fieldList", fieldList);
        param.put("typeSet", typeSet);
        param.put("readOnly", readOnly);
        System.out.println("组装参数：" + param);

        genJava(Domain, param, "service", "service");
        genJava(Domain, param, "controller/admin", "adminController");
        genJava(Domain, param, "req", "saveReq");
        genJava(Domain, param, "req", "queryReq");
        genJava(Domain, param, "resp", "queryResp");
    }

    private static void genJava(String Domain, Map<String, Object> param, String packageName, String target) throws IOException, TemplateException {
        FreemarkerUtil.initConfig(target + ".ftl");
        String toPath = serverPath + packageName + "/";
        new File(toPath).mkdirs();
        // 目标类名首字符大写
        String Target = target.substring(0, 1).toUpperCase() + target.substring(1);
        String fileName = toPath + Domain + Target + ".java";
        System.out.println("开始生成：" + fileName);
        FreemarkerUtil.generator(fileName, param);
    }

    private static void genVue(String Domain, Map<String, Object> param) throws IOException, TemplateException {
        FreemarkerUtil.initConfig("vue.ftl");
        new File(vuePath + module).mkdirs();
        String fileName = vuePath + module + "/" + Domain + ".vue";
        System.out.println("开始生成：" + fileName);
        FreemarkerUtil.generator(fileName, param);
    }

    private static String getGeneratorPath() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Map<String, String> map = new HashMap<String, String>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(pomPath);
        Node node = document.selectSingleNode("//pom:configurationFile");
        System.out.println(node.getText());
        return node.getText();
    }

    /**
     * 获取所有的Java类型，使用Set去重
     */
    private static Set<String> getJavaTypes(List<Field> fieldList) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            set.add(field.getJavaType());
        }
        return set;
    }
}
