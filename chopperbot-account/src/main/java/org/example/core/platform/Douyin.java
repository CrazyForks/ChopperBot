package org.example.core.platform;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import org.example.core.factory.PlatformOperation;
import org.example.mapper.AccountMapper;
import org.example.core.pojo.Account;
import org.openqa.selenium.Cookie;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @Description
 * @Author welsir
 * @Date 2023/10/9 17:09
 */
public class Douyin implements PlatformOperation {

    final String FILE_PATH = "D:\\Douyincookies.txt";

    @Resource
    AccountMapper accountMapper;
    @Override
    public void login(int id,String username,String password) {
        try {
            List<String> command = new ArrayList<>();
            command.add("python"); // Python 解释器
            command.add("D:\\code\\gitHubProject\\Text_select_captcha\\DouyinLogin.py"); // 要运行的 Python 脚本文件名
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            List<String> command1 = new ArrayList<>();
            command1.add("python"); // Python 解释器
            command1.add("D:\\code\\gitHubProject\\Text_select_captcha\\Douyin.py");
            ProcessBuilder processBuilder1 = new ProcessBuilder(command1);
            Process process1 = processBuilder1.start();
            // 获取Python脚本的标准输出流
            InputStream inputStream1 = process1.getInputStream();
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream1));

            String line1;
            StringBuilder callBack = new StringBuilder();

            // 读取Python脚本的输出
            while ((line1 = reader1.readLine()) != null) {
                callBack.append(line1).append("\n");
            }
            System.out.println("验证flag:" + callBack);
            // 等待Python脚本执行完成
            int exitCode = process1.waitFor();
            if (exitCode == 0) {
                System.out.println("抖音登录成功!");
                Account account = new Account();
                account.setUsername(username);
                account.setCookies(loadCookiesFromFile(FILE_PATH).toString());
                account.setPlatformId(2);
                accountMapper.insert(account);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public HashSet<Cookie> loadCookiesFromFile (String filePath) throws IOException {
        HashSet<Cookie> cookies = new HashSet<>();
        try (FileReader fileReader = new FileReader(filePath)) {
            JSONReader reader = new JSONReader(fileReader);
            JSONArray jsonArray = reader.readObject(JSONArray.class);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String value = jsonObject.getString("value");
                String domain = jsonObject.getString("domain");
                String path = jsonObject.getString("path");
                String sameSite = jsonObject.getString("sameSite");
                Date expiry = jsonObject.getDate("expiry");
                boolean secure = jsonObject.getBoolean("secure");
                boolean httpOnly = jsonObject.getBoolean("httpOnly");
                Cookie cookie = new Cookie(name, value, domain, path, expiry, secure, httpOnly, sameSite);
                cookies.add(cookie);
            }
        }
        return cookies;
    }
}
