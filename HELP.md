# 常见问题汇总
### generator代码生成器运行报错，找不到member中的枚举类
generator模块依赖member模块, member模块依赖common模块。所以先编译 common，再编译 member， 就可以执行generator了。
