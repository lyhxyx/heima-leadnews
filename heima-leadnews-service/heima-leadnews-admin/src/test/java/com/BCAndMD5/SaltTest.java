package com.BCAndMD5;

import com.heima.utils.common.BCrypt;
import org.junit.Test;

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
}
