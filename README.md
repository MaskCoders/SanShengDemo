# SanShengDemo
导航、数据库、socket

开发在自己分支，然后合并到master

按目前了解的需求制作一个完整的可运行的app程序

1. 首页，要包含功能导航
2. sqlite数据库表操作展示，制作内容展示，可无限下拉和编辑页（可编辑每条记录的内>容），表结构如下

[Id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL         标识
,[MeterId] INTEGER NOT NULL      电表标识
,[MeterName] CHAR(20) NOT NULL   电表名称  显示为文本
,[ValueTime] DATETIME NOT NULL   数据时标  按长时间格式显示 如2001-1-1 01:01:01
,[ReadTime] DATETIME NOT NULL    读取时间  同上
,[DtId] INTEGER NOT NULL         数据类型  显示值对应的文本 *注1
,[Valz] FLOAT NOT NULL
,[IsImportant] BOOLEAN           是否重要

注1：数据类型DtId的值 1显示为“日冻结”  2显示为“实时数据”

3. 实现一个基于socket（Tcp）的服务，侦听8001端口，可以通过网络连接，连接后可以收
发二进制数据，收发的报文可以显示在界面上
比如：客户端发送 2个字节报文 0x68 0x16 服务端返回0x16 0x68


