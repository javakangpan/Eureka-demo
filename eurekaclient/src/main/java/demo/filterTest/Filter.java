 package demo.filterTest;

 public interface Filter {
     
     public void doFilter(Request request,Response response,FilterChain chain);

}
