import serial
import numpy
import matplotlib.pyplot as plt
from drawnow import *

xValueList = []
yValueList = []
zValueList = []


arduinoData = serial.Serial('/dev/cu.wchusbserial1420', 9600) # for nano

plt.ion()

def makeFig():
	plt.plot(xValueList, 'b')
	plt.plot(yValueList, 'r')

while True:
	try:	
		arduinoString = arduinoData.readline()
		dataArray = arduinoString.split('\t')
		if(len(dataArray) == 3):
			ax = float(dataArray[0])
			ay = float(dataArray[1])
			xValueList.append(ax)
			yValueList.append(ay)
			print ax, ' , ' ,  ay
			drawnow(makeFig)
		else:
			print 'error'
	except ValueError:
		print 'error'
		
