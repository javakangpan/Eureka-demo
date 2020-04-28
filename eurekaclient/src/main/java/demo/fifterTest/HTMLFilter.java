 package demo.fifterTest;


public class HTMLFilter implements Filter {
 
    @Override
    public void doFilter(Request request, Response response, FilterChain chain) {
        request.requestStr = request.requestStr.replace("<", "[").replace(">", "]") + "--------HTML Request Filter";
        System.out.println("HTML Filter request Str:" + request.requestStr);
        chain.doFilter(request, response, chain);
        response.responseStr = response.responseStr + "-------------HTML response filter";
        System.out.println("HTML Filter response Str:" + response.responseStr);
    }
}
