from sklearn import datasets

import numpy as np
from sklearn import svm
import matplotlib.pyplot as plt
from sklearn.datasets import load_svmlight_file
from sklearn.metrics import confusion_matrix
from sklearn.naive_bayes import GaussianNB
from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import LinearSVC
from sklearn.calibration import calibration_curve
from sklearn.neighbors import KNeighborsClassifier
from sklearn.discriminant_analysis import QuadraticDiscriminantAnalysis
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis
from sklearn.cross_validation import cross_val_score
from sklearn.tree import DecisionTreeClassifier
from sklearn.neighbors import NearestNeighbors
from sklearn.svm import SVC
from sklearn.ensemble import RandomForestClassifier


charToInt = {}
charToInt['0'] = 0
charToInt['1'] = 1
charToInt['2'] = 2
charToInt['3'] = 3
charToInt['4'] = 4
charToInt['5'] = 5
charToInt['6'] = 6
charToInt['7'] = 7
charToInt['8'] = 8
charToInt['9'] = 9
charToInt['a'] = 10
charToInt['b'] = 11
charToInt['c'] = 12
charToInt['d'] = 13
charToInt['e'] = 14
charToInt['f'] = 15
charToInt['g'] = 16
charToInt['h'] = 17
charToInt['i'] = 18
charToInt['j'] = 19
charToInt['k'] = 20
charToInt['l'] = 21
charToInt['m'] = 22
charToInt['n'] = 23
charToInt['o'] = 24
charToInt['p'] = 25
charToInt['q'] = 26
charToInt['r'] = 27
charToInt['s'] = 28
charToInt['t'] = 29
charToInt['u'] = 30
charToInt['v'] = 31
charToInt['w'] = 32
charToInt['x'] = 33
charToInt['y'] = 34
charToInt['z'] = 35
charToInt['.'] = 36
charToInt[' '] = 37
charToInt['\x08'] = 38

intToChar = {}
intToChar[0] = '0'
intToChar[1] = '1'
intToChar[2] = '2'
intToChar[3] = '3'
intToChar[4] = '4'
intToChar[5] = '5'
intToChar[6] = '6'
intToChar[7] = '7'
intToChar[8] = '8'
intToChar[9] = '9'
intToChar[10] = 'a'
intToChar[11] = 'b'
intToChar[12] = 'c'
intToChar[13] = 'd'
intToChar[14] = 'e'
intToChar[15] = 'f'
intToChar[16] = 'g'
intToChar[17] = 'h'
intToChar[18] = 'i'
intToChar[19] = 'j'
intToChar[20] = 'k'
intToChar[21] = 'l'
intToChar[22] = 'm'
intToChar[23] = 'n'
intToChar[24] = 'o'
intToChar[25] = 'p'
intToChar[26] = 'q'
intToChar[27] = 'r'
intToChar[28] = 's'
intToChar[29] = 't'
intToChar[30] = 'u'
intToChar[31] = 'v'
intToChar[32] = 'w'
intToChar[33] = 'x'
intToChar[34] = 'y'
intToChar[35] = 'z'
intToChar[36] = '.'
intToChar[37] = ' '
intToChar[38] = '\x08'


c = 0.0001
avgs = []
nearestNeighbors = 1
oneTime = 0
nEstimators = 1

while(nEstimators < 51):

	oneTime = 1

	clf = RandomForestClassifier(n_estimators=nEstimators)

	avg = 0


	for sample in range(0, 5):

		trainingFile = "/Users/omarali/Documents/Bachelor/DataSets/ExperimentationDataSetsAll/TrainingSet" + str(sample) + ".txt"
		testingFile = "/Users/omarali/Documents/Bachelor/DataSets/ExperimentationDataSetsAll/TestingSet" + str(sample) + ".txt"


		X_train, y_train = load_svmlight_file(trainingFile)

		X_test, y_test = load_svmlight_file(testingFile)

		clf.fit(X_train, y_train)


		y_true = []
		y_pred = []



		i = 0
		total = 0.0
		passed = 0.0
		failed = 0.0
		passedPercentage = 0

		while(i < len(y_test)):
			res = clf.predict(X_test[i])
			if(res[0] == y_test[i]):
				mark = 'passed'
				passed += 1
			else:
				mark = 'failed, should be ', chr(int(y_test[i]))
				failed += 1

			total += 1
			y_true += [chr(int(y_test[i]))]
			y_pred += [chr(int(res[0]))]
			i += 1

		passedPercentage = passed / total * 100

		avg += passedPercentage


	avg /= 5
	print 'n_estimators =   ', nEstimators,  '----------> Average Accuracy: ', avg, '%'
	c *= 10
	nearestNeighbors += 1
	nEstimators += 1




