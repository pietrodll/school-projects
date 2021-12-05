# -*- coding: utf-8 -*-

from sklearn import cross_validation
from sklearn.metrics import r2_score, mean_squared_log_error, mean_absolute_error, mean_squared_error
import numpy as np
import matplotlib.pyplot as plt
from scipy.integrate import simps

def crossValidation(X, y, classfunction, errorFunction=mean_squared_log_error, display=False):
    kf = cross_validation.KFold(X.shape[0], n_folds=8)
    
    errors = [] # Variable containing errors for each fold
    scores = [] # Variable containing the R^2 scores for each fold
    totalInstances = 0
    totalError = 0
    
    for trainIndex, testIndex in kf:
        trainSet = X[trainIndex]
        testSet = X[testIndex]
        trainLabels = y[trainIndex]
        testLabels = y[testIndex]
            	
        predictedLabels = classfunction(trainSet, trainLabels, testSet)
        
        error = errorFunction(testLabels, predictedLabels)
        totalError += error*testIndex.shape[0]
        totalInstances += testIndex.shape[0]
        
        errors.append(error)
        scores.append(r2_score(testLabels, predictedLabels))
    
    if display:
        print('Error : ', np.around(totalError/totalInstances, 5))
        # print('Errors : ', np.around(errors, 2))
        # print('Scores : ', np.around(scores, 2))
    
    return totalError/totalInstances


def plotFeatureSelection(X, y, regressionFunction, featureFunction):
    error = [0]*X.shape[1]
    for k in range(1, X.shape[1]):
        Xnew = featureFunction(X, y, k)
        error[k-1] = crossValidation(Xnew, y, regressionFunction)
    error[-1] = crossValidation(X, y, regressionFunction)
    plt.plot(np.arange(1, X.shape[1]+1), error, label=regressionFunction.__name__)


def classifCrossValidation(X, y, classfunction, display=False):
    kf = cross_validation.KFold(X.shape[0], n_folds=10)
    
    acc = []
    falseNeg = []
    totalInstances = 0 # Variable that will store the total intances that will be tested  
    totalCorrect = 0   # Variable that will store the correctly predicted intances  
    totalFalseNeg = 0
    
    for trainIndex, testIndex in kf:
        trainSet = X[trainIndex]
        testSet = X[testIndex]
        trainLabels = y[trainIndex]
        testLabels = y[testIndex]
            	
        predictedLabels = classfunction(trainSet, trainLabels, testSet)
    
        correct = 0
        fn = 0
        
        for i in range(testSet.shape[0]):
            if predictedLabels[i] == testLabels[i]:
                correct += 1
            elif predictedLabels[i] == 0 and testLabels[i] == 1:
                fn += 1
            
        acc.append(float(correct)/testLabels.size)
        falseNeg.append(float(fn)/testLabels.size)
        totalCorrect += correct
        totalInstances += testLabels.size
        totalFalseNeg += fn
    
    if display:
        print('Total accuracy : ', np.around(totalCorrect/totalInstances, 5))
        #print('Accuracy : ', np.around(acc, 2))
        print('Total False Negative Rate : ', np.around(totalFalseNeg/totalInstances, 5))
        #print('False Negative Rates : ', np.around(falseNeg, 2))

def REC(y_true , y_pred):
    
    # initilizing the lists
    Accuracy = []
    
    # initializing the values for Epsilon
    Begin_Range = 0
    End_Range = 1.5
    Interval_Size = 0.01
    
    # List of epsilons
    Epsilon = np.arange(Begin_Range , End_Range , Interval_Size)
    
    # Main Loops
    for i in range(len(Epsilon)):
        count = 0.0
        for j in range(len(y_true)):
            if np.linalg.norm(y_true[j] - y_pred[j]) / np.sqrt( np.linalg.norm(y_true[j]) **2 + np.linalg.norm(y_pred[j])**2 ) < Epsilon[i]:
                count = count + 1
        
        Accuracy.append(count/len(y_true))
    
    # Calculating Area Under Curve using Simpson's rule
    AUC = simps(Accuracy , Epsilon ) / End_Range
        
    # returning epsilon , accuracy , area under curve    
    return Epsilon , Accuracy , AUC
