from __future__ import print_function
import sys
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

open("/Users/omarali/Documents/Bachelor/DataSets/SignalFile.txt", 'w').close()


X_train, y_train = load_svmlight_file("/Users/omarali/Documents/Bachelor/DataSets/DataSetShuffled.txt")

clf = svm.SVC(kernel = 'linear', C = 100)
clf.fit(X_train, y_train)

f = open("/Users/omarali/Documents/Bachelor/DataSets/SignalFile.txt",'r')

while(True):
	x = f.readline()
	if(x != ""):
		X_test, y_test = load_svmlight_file("/Users/omarali/Documents/Bachelor/Datasets/WrittenCharacter.txt")
		res = clf.predict(X_test[0])
		if(int(res[0]) == 8):
			sys.stdout.write('\b \b')
		else:
			sys.stdout.write(chr(int(res[0])))
		sys.stdout.flush()
