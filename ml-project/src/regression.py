# -*- coding: utf-8 -*-

import numpy as np
import matplotlib.pyplot as plt
from sklearn.neighbors import KNeighborsRegressor
from sklearn import linear_model, neural_network, svm
from validation import crossValidation, plotFeatureSelection
import preprocessing as pre

X, y = np.array(pre.X), np.array(pre.y)
X1 = np.array(pre.X1)
X2 = np.array(pre.X2)
X3 = np.array(pre.X3)
# X = pre.featureSelect(X, y, 9)

def KNNregression(trainSet, trainLabels, testSet):
    N = len(trainSet)
    trainLabels = np.log(trainLabels) # logarithmic function
    neigh = KNeighborsRegressor(n_neighbors=int(np.sqrt(N)), weights='distance')
    neigh.fit(trainSet, trainLabels)
    y = neigh.predict(testSet)
    return np.exp(y)

def SVMregression(X, y, Xtest):
    clf = svm.SVR(kernel='rbf')
    clf.fit(X, y)
    ytest = clf.predict(Xtest)
    return ytest
    
def linearRegression(trainSet, trainLabels, testSet):
    regr = linear_model.LinearRegression()
    regr.fit(trainSet,np.log(trainLabels))
    return np.exp(regr.predict(testSet))

def stochGrad(trainSet, trainLabels, testSet):
    regr = linear_model.SGDRegressor()
    regr.fit(trainSet,np.log(trainLabels))
    return np.exp(regr.predict(testSet))

def neuralNetwork(trainSet, trainLabels, testSet):
    neu=neural_network.MLPRegressor()
    neu.fit(trainSet,trainLabels)
    return neu.predict(testSet)

regressors = [SVMregression]
regressors = [SVMregression, KNNregression, linearRegression, stochGrad]

def compareRegressors(X, y, regressors, featureFunction=pre.featureSelect):
    plt.figure()
    for func in regressors:
        plotFeatureSelection(X, y, func, featureFunction)
    plt.legend()


#plt.figure()

#compareRegressors(X, y, regressors)
#plt.title('X')
#compareRegressors(X1, y, regressors)
#plt.title('X1')
#compareRegressors(X2, y, regressors)
#plt.title('X2')
#compareRegressors(X3, y, regressors)
#plt.title('X3')


def displayResult(X, y, regressors, k=None, featureFunction=pre.featureSelect):
    if k != None:
        Xnew = featureFunction(X, y, k)
    else:
        Xnew = X
    for func in regressors:
        print(func.__name__ + " : ")
        crossValidation(Xnew, y, func, display=True)

print('X3')
displayResult(X3, y, regressors)
print('X2')
displayResult(X2, y, regressors)
print('X1')
displayResult(X1, y, regressors)
print('X')
displayResult(X, y, regressors)

