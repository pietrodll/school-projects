# -*- coding: utf-8 -*-

from sklearn.svm import SVR
from sklearn.naive_bayes import GaussianNB
import preprocessing as pre
import numpy as np
from sklearn.model_selection import train_test_split
from validation import REC
import matplotlib.pyplot as plt

X = np.array(pre.C)
y = np.array(pre.data['area'])

def totalRegression(X, y, Xtest):
    y_clas = np.array(y > 0, dtype=np.int)
    Xpos = X[y > 0]
    ypos = y[y > 0]
    gnb = GaussianNB()
    gnb.fit(X, y_clas)
    y_clas_pred = gnb.predict(Xtest)
    svr = SVR()
    svr.fit(Xpos, ypos)
    ytest = []
    for i in range(Xtest.shape[0]):
        if y_clas_pred[i] == 1:
            ytest.append(svr.predict(Xtest[i].reshape(1, -1)))
        else:
            ytest.append(0)
    return ytest

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3)

y_pred = totalRegression(X_train, y_train, X_test)

eps, acc, auc = REC(y_test, y_pred)

plt.plot(eps, acc)
