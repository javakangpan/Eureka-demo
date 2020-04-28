 package demo.fifterTest;

 public class SensitiveFilter implements Filter {
     
     @Override
     public void doFilter(Request request, Response response, FilterChain chain) {
         request.requestStr = request.requestStr + "---------------Sensitive request Filter";
         System.out.println("sensitiveFilter request str:" + request.requestStr);
         chain.doFilter(request, response, chain);
         response.responseStr = response.responseStr + "---------------------sensitive response filter";
         System.out.println("sensitiveFilter response str:" + response.responseStr);
     }
}