:: 关闭回显，只显示结果
@echo off
:: 设置执行文件名为标题
title %0

:: 脚本逻辑请在此添加
java -cp "..\dependency\*;.\classes;%CLASSPATH%" com.nicchagil.test.ProduceQueueMsgTest

:: 执行完等待输入
pause
