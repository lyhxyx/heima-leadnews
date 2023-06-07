package com.heima.user;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.heima.user.mapper")
@EnableFeignClients(basePackages = "com.heima.apis")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}


class aa{
    public static void main(String[] args) {
        BigDecimal a=BigDecimal.ZERO;
        List<BigDecimal> list = new ArrayList<>();
        list.add(new BigDecimal("20"));
        list.add(new BigDecimal("20"));
        list.add(new BigDecimal("20"));
        for (int i = 0; i < list.size(); i++) {
            a=a.add(list.get(i));
        }
        System.out.println(a);
    }
}

class bb{
    //break  终止循环   continue  结束本次循环进行下次循环
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 0, 3, 4, 5);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i)==0){
                continue;
            }
            System.out.println(list);
        }
    }
}

class MYtask implements Callable<Integer>{
    private int upperBounds;
    @Override
    public Integer call() throws Exception {
        int sum=0;
        for (int i = 0; i < upperBounds; i++) {
            sum+=i;
        }
        return sum;
    }
    public MYtask(int upperBounds){
        this.upperBounds=upperBounds;
    }
}

class test01{
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ArrayList<Future<Integer>> list = new ArrayList<>();
        ExecutorService service= Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            list.add(service.submit(new MYtask((int)(Math.random()*100))));
        }
        int sum=0;
        for (Future<Integer> future : list) {
            sum+=future.get();
        }
        System.out.println(sum);
    }
}

class dd{
    public static void main(String[] args) {
        int x=0;
        int y=0;
        int k=0;
        for (int z = 0; z < 5; z++) {
            if ((++x>2)&&(++y>2)&&(k++>2)){
                x++;
                ++y;
                k++;
            }
        }

        System.out.println(x+""+y+""+k);
    }
}

class nn{
    private String a;
    private String b;
    private String c;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public nn(String a, String b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public String toString() {
        return "nn{" +
                "a='" + a + '\'' +
                ", b='" + b + '\'' +
                ", c='" + c + '\'' +
                '}';
    }
}
class xianjin{

    private String r;
    private String d;

    public xianjin(String r, String d, String n) {
        this.r = r;
        this.d = d;
        this.n = n;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    private String n;

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }
}
class ff{
    public static void main(String[] args) {
     List<aa> reder=new ArrayList();
        ArrayList<xianjin> xianjin = new ArrayList<>();
      /*  for (int i = 0; i < reder.size(); i++) {
            xianjin xianjin1 = new xianjin();

        }*/
    }
}


class zhinengxin{
    public static void main(String[] args) {
        if (客户账号==null){
            去智能薪支付信息主表里面根据机构号查到所有的账号;
        }
        用客户账号在判断批次输入和不输入的时候，输入的时候
    }
}
