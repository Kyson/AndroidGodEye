/**
    classInfo
        {int access
         String name
         String superName
         String[] interfaces}

     methodInfo
         {int access
         String name
         String desc}
**/
function isInclude(classInfo,methodInfo){
    return classInfo.name.startsWith('cn/hikyson/godeye/sample')
}