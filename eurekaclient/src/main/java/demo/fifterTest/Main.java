 package demo.fifterTest;

 public class Main {
     public static void main(String[] args) {
         
         String msg = "<html>testMsg</html>";
         Request request = new Request();
         request.requestStr = msg;
         Response response = new Response();
         response.responseStr = "responseStr";
         
         FilterChain fc = new FilterChain();
         fc.addFilter(new HTMLFilter()).addFilter(new SensitiveFilter());
         fc.doFilter(request, response, fc);
  
     }
//15071100713 陈师傅 鄂A11900D 宝通寺地铁A出口
     //30 赛意新员工入职的 联投时代 积玉桥这边
}
