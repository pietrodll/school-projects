# -*- coding: utf-8 -*-

import numpy as np
import preprocessing as pre
from validation import classifCrossValidation
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis
from sklearn.neighbors import KNeighborsClassifier
from sklearn.svm import SVC
from sklearn.naive_bayes import GaussianNB
from sklearn.ensemble import AdaBoostClassifier
from sklearn.tree import DecisionTreeClassifier


X, y = np.array(pre.C), np.array(pre.c)
X1 = np.array(pre.C1)
X2 = np.array(pre.C2)
X3 = np.array(pre.C3)

def LDA(X, y, Xtest):
    lda = LinearDiscriminantAnalysis()
    lda.fit(X, y)
    ytest = lda.predict(Xtest)
    return ytest

def KNN(X, y, Xtest):
    N = X.shape[0]
    knn = KNeighborsClassifier(n_neighbors = int(np.sqrt(N))//2, weights='distance')
    knn.fit(X, y)
    ytest = knn.predict(Xtest)
    return ytest

def SVM(X, y, Xtest):
    svc = SVC(C=1.0, kernel='rbf')
    svc.fit(X, y)
    ytest = svc.predict(Xtest)
    return ytest

def GNB(X, y, Xtest):
    gnb = GaussianNB()
    gnb.fit(X, y)
    ytest = gnb.predict(Xtest)
    return ytest

def tree(X, y, Xtest):
    dtc = DecisionTreeClassifier()
    dtc.fit(X, y)
    ytest = dtc.predict(Xtest)
    return ytest

def AdaBoost(X, y, Xtest):
    abc = AdaBoostClassifier(n_estimators=50)
    abc.fit(X, y)
    ytest = abc.predict(Xtest)
    return ytest

def displayResults(X, y, regressors):
    for func in regressors:
        print(func.__name__ + " : ")
        classifCrossValidation(X, y, func, display=True)

# func = [KNN, LDA, SVM, GNB]
func = [GNB, AdaBoost, tree, SVM]

print('X3')
displayResults(X3, y, func)
print('X2')
displayResults(X2, y, func)
print('X1')
displayResults(X1, y, func)
print('X')
displayResults(X, y, func)
