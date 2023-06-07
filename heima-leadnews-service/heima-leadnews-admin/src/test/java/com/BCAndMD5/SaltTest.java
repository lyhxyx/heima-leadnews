package com.BCAndMD5;

import com.heima.admin.utils.BCrypt;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.springframework.util.DigestUtils;

/**
 * @ProjectName: heima-leadnews-371-lhy
 * @Package: com.BCAndMD5
 * @Description:
 * @Author: 李昊阳
 * @CreateDate: 2022/4/1 17:27
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2022
 */
public class SaltTest {
   @Test
    public void Test(){
       String hashpw = BCrypt.hashpw("123456", BCrypt.gensalt());
       System.out.println(hashpw);
       boolean checkpw = BCrypt.checkpw("123456", "$2a$10$NvyedZTnMtVnxx4pH3wbv.D7ugC1FY0A4LpjlzQWHdcxUnkQenAPK");
       System.out.println(checkpw);
   }

   /**
    * md5直接加密测试（放弃使用）
    */
   @Test
   public void testMd5(){
      for (int i = 0; i < 10; i++) {
         String pwdText = "123456"; //明文密码
         String pwdEncrypt = DigestUtils.md5DigestAsHex(pwdText.getBytes()); //MD5直接加密后的密码
         System.out.println(pwdEncrypt);
      }
   }

   /**
    * md5加盐的加密测试
    */
   @Test
   public void testMd5AndSalt(){
      for (int i = 0; i < 10; i++) {
         String pwdText = "123456"; //明文密码
         String randomStr = RandomStringUtils.randomAlphanumeric(10);//生成随机字符串

         //将明文密码加盐进行拼接
         String pwd = pwdText + randomStr;

         //使用md5进行加密
         String pwdEncrypt = DigestUtils.md5DigestAsHex(pwd.getBytes());
         System.out.println("明文："+pwdText+",随机数："+randomStr+",加密后的结果："+pwdEncrypt);
      }
   }

   /**
    * Bcrypt加密测试
    */
   @Test
   public void testBcryptEncrpt(){
      for (int i = 0; i < 10; i++) {
         String pwdText = "123456"; //明文密码
         String salt = BCrypt.gensalt();
         String pwdEncrypt = BCrypt.hashpw(pwdText, BCrypt.gensalt());
         System.out.println("盐："+salt+",加密后的密码：" + pwdEncrypt);
      }
   }


   /**
    * Bcrypt密码校验测试
    * 加密后的密码：$2a$10$Jyz7PNe4AfN0PwXAvRwXq.gHaJOZN9eDSJ1RhHPRR8n/.sl5VFmlW
    * 加密后的密码：$2a$10$ERlYOpGrA4bZuFqNBOeHRubB.oIyx9ASA/LZTFggMgP5S39cuFePK
    * 加密后的密码：$2a$10$YewKsYOJ9ZEvurGx3sILveKM1DIQ6tDkBTAl1/fVMeF3uwA6nRNBm
    */
   @Test
   public void testBcryptCheck(){
      String pwdText = "123456"; //明文密码
      //数据库中的密文密码
      String[] pwdArr = {"$2a$10$Jyz7PNe4AfN0PwXAvRwXq.gHaJOZN9eDSJ1RhHPRR8n/.sl5VFmlW","$2a$10$ERlYOpGrA4bZuFqNBOeHRubB.oIyx9ASA/LZTFggMgP5S39cuFePK","$2a$10$YewKsYOJ9ZEvurGx3sILveKM1DIQ6tDkBTAl1/fVMeF3uwA6nRNBm"};
      for (String pwdDB : pwdArr) {
         boolean result = BCrypt.checkpw(pwdText, pwdDB);
         System.out.println("登录校验是否成功：" + result);
      }
   }
}
