# MYJDBC

#### 介绍
轻量级ORM框架，兼容JPA规范

#### 软件架构


#### 软件使用方法

一、直接使用BaseService
   
    @Autowired
    private BaseService baseService; 
    
    
    //直接使用
    List<TSUser> userPoList = baseService.findAll(TSUser.class);
    


     