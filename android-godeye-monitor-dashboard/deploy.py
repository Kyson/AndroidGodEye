#!/usr/bin/python
# -*- coding: utf-8 -*-

import os
import time
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

def copyFiles(sourceDir, targetDir):   
    for f in os.listdir(sourceDir):
        sourceF = os.path.join(sourceDir, f)
        targetF = os.path.join(targetDir, f)
        if os.path.isfile(sourceF):
            #创建目录   
            if not os.path.exists(targetDir):
                os.makedirs(targetDir)
            #文件不存在，或者存在但是大小不同，覆盖
            if not os.path.exists(targetF) or (os.path.exists(targetF) and (os.path.getsize(targetF) != os.path.getsize(sourceF))):
                open(targetF, "wb").write(open(sourceF, "rb").read())
                print u"[%s] copy to [%s] ok." %(sourceF,targetF)
            else:
                print u"[%s] already exist,copy canceled." % targetF
        if os.path.isdir(sourceF):   
            copyFiles(sourceF, targetF)

def del_dir_tree(path):
    ''' 递归删除目录及其子目录,　子文件'''
    if os.path.isfile(path):
        try:
            os.remove(path)
            print u"[%s] deleted." % path
        except Exception, e:
            #pass
            print e
    elif os.path.isdir(path):
        for item in os.listdir(path):
            itempath = os.path.join(path, item)
            del_dir_tree(itempath)
        try:
            os.rmdir(path)   # 删除空目录
            print u"[%s] empty dir deleted." % path
        except Exception, e:
            #pass
            print e

if __name__ == "__main__":
    currentDir = os.path.dirname(os.path.realpath(__file__))
    currentParentDir = os.path.dirname(currentDir)
    sourceDir = os.path.join(currentDir,"build")
    targetDir = os.path.join(currentParentDir,"android-godeye-monitor/src/main/assets/android-godeye-dashboard")
    print u"source dir: [%s]"%sourceDir
    print u"target dir: [%s]"%targetDir
    del_dir_tree(targetDir)
    copyFiles(sourceDir,targetDir)