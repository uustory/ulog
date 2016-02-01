#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
# Android调试日志服务器
# 
# 需要安装web.py库，easy_install web.py 或者pip install web.py
# 
# 启动游戏之前先，运行python uconsole.py 启动日志控制台服务器
# 默认端口是8080，启动之后，可以浏览器中访问http://localhost:8080
# 后面的日志就可以在网页中来查看了，网页会自动刷新
#
# 客户端中需要配置日志打印相关参数，
# 如果不是使用的U8SDK打包工具进行打包的，需要在AndroidManifest.xml中配置对应的参数和控制台的地址
# 如果使用的是U8SDK打包工具进行打包，那么可以在打包工具目录/games/games.xml中进行配置
#
#

import sys
import argparse
import stat
import json
import web

web.config.debug = False

urls = (
		'/','index'
	)

localLogs = ""

def storeLogs(content):

	if content.startswith('{') and content.endswith('}'):
		content = '[' + content + ']'
	logs = json.loads(content)

	for log in logs:

		if 'stack' not in log:
			log['stack'] = " "

		color = '#808080'
		if log['level'] == 'INFO':
			color = '#008000'
		elif log['level'] == 'WARNING':
			color = '#FFA500'
		elif log['level'] == 'ERROR':
			color = '#FF0000'

		strLog = '<div style="color:%s">%s  %s: [%s] %s </div>' % (color, log['time'],log['level'], log['tag'], log['msg'])

		stacks = log['stack'].split('\n')
		strLog = strLog + ('<div color="%s">' % color)
		for s in stacks:
			strLog = strLog + ('<div>%s</div>' % (s.strip()))

		strLog = strLog + '</div>'
		global localLogs
		localLogs = localLogs + strLog
		

def encodeHtml(content):

	htmlFormat = "<html><head><title></title></head><body>%s   <script type=\"text/javascript\">function myrefresh(){window.location.reload();window.scrollTo(0,document.body.scrollHeight);}setTimeout('myrefresh()',1000); </script></body></html>"	
	content = content.encode('gbk')
	return htmlFormat % content


class index:

	def GET(self):

		global localLogs

		return encodeHtml(localLogs)

	def POST(self):

		content=web.input()
		logs = content.get('log')
		storeLogs(logs)
		return ""


if __name__ == '__main__':

	app = web.application(urls, globals())
	app.run()
