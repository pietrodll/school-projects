# -*- coding: utf-8 -*-

import pandas as pd
import numpy as np
from sklearn.feature_selection import SelectKBest, f_regression
import matplotlib.pyplot as plt


def setMonths2(data):
    """
    data: pandas DataFrame
    Replaces the month feature by 12 features, one for each month, which can only be 1 or 0.
    """
    months = ['jan', 'feb', 'mar', 'apr', 'may', 'jun', 'jul', 'aug', 'sep', 'oct', 'nov', 'dec']
    for month in months:
        new_column = pd.Series(0, index=data.index)
        new_column[data.month == month] = 1
        data[month] = new_column
    data.drop('month', axis=1, inplace=True)


def setDays2(data):
    """
    data: pandas DataFrame
    Replaces the month feature by 12 features, one for each month, which can only be 1 or 0.
    """
    days = ['sun', 'mon', 'tue', 'wed', 'thu', 'fri', 'sat']
    for day in days:
        new_column = pd.Series(0, index=data.index)
        new_column[data.day == day] = 1
        data[day] = new_column
    data.drop('day', axis=1, inplace=True)

def setWeekend(data, deleteDay=True):
    """
    data: pandas DataFrame
    Replaces the day feature by a weekend feature, whose value is 1 if the day is Saturday or Sunday, 0 otherwise.
    """
    new_col = pd.Series(0, index=data.index)
    new_col[data['day'].isin(['sat', 'sun'])] = 1
    data['weekend'] = new_col
    if deleteDay:
        data.drop('day', axis=1, inplace=True)


def setDays(data):
    """
    data : pandas DataFrame
    Replaces the name of the day of the week by a number
    """
    days = {'sun':7, 'mon':1, 'tue':2, 'wed':3, 'thu':4, 'fri':5, 'sat':6}
    a = pd.Series(index=data.index)
    for d in days.keys():
        a[data.day == d] = days[d]
    data.day = a


def setMonths(data):
    """
    data : pandas DataFrame
    Replaces the name of the month by a number
    """
    months = {'jan':1, 'feb':2, 'mar':3, 'apr':4, 'may':5, 'jun':6, 'jul':7, 'aug':8, 'sep':9, 'oct':10, 'nov':11, 'dec':12}
    a = pd.Series(index=data.index)
    for m in months.keys():
        a[data.month == m] = months[m]
    data.month = a


def scaleData(X):
    """
    X : pandas DataFrame with only numerical values
    This function linearly scales the features of X, to make each value lie between 0 and 1
    """
    M, m = X.max(), X.min()
    for col in X.columns:
        if M[col] != m[col]:
            X[col] = (X[col] - m[col])/(M[col] - m[col])


def normalizeData(X, columns='all'):
    """
    X : pandas DataFrame with only numerical values
    This function linearly scales the features of X, to make it centered with a unitary standard deviation
    """
    M, S = X.mean(), X.std()
    if columns == 'all':
        colList = X.columns
    else:
        colList = columns
    for col in colList:
        if S[col] == 0:
            X[col] = 0
        else:
            X[col] = (X[col] - M[col])/S[col]


def PCA(A, y, k):
    """
    A : Numpy Array, k : integer
    Performs PCA on A
    """
    M = np.tile(np.average(A, axis=0), (A.shape[0], 1)) # Mean of the columns
    C = A - M
    W = np.dot(np.transpose(C), C)
    _, eigvec = np.linalg.eigh(W)
    eigvec = eigvec[:,::-1] # eigenvalues in ascending order : colums of U must be reversed
    Uk = eigvec[:,:k]
    return np.dot(A, Uk)


def featureSelect(X, y, j):
    Xnew = SelectKBest(f_regression, k=j).fit_transform(X, y)
    return Xnew


def plotData(X, y, features=None, sepFig=False, log=False):
    a = np.array(y)
    if features == None:
        colList = X.columns
    else:
        colList = features
    for col in colList:
        if sepFig:
            plt.figure()
            plt.title(col)
        A = np.array(X[col])
        if log:
            a = np.log(a)
        plt.plot(A, a, '+', label=col)
    if not sepFig:
        plt.legend()

def describeData(X, y, features=None, log=False):
    """
    X: pandas DataFrame of the input vectors
    y: pandas DataFrame of the labels
    features: list of the features to describe (they have to be categorical)
    Returns a pandas DataFrame with statistical information about y for each possible value of the feature
    """
    if log:
        a = np.log(y)
    else:
        a = y
    if features == None:
        colList = X.columns
    else:
        colList = features
    D = pd.DataFrame()
    for col in colList:
        values = pd.unique(X[col])
        for val in values:
            D[col + ' = ' + str(val)] = a[X[col] == val].describe()
    return D


def setCategorical(X, col, deleteCol=True):
    """
    X: pandas DataFrame
    col: str
    Generalization of setDays2 and setMonth2
    """
    values = pd.unique(X[col])
    for val in values:
        new_col = pd.Series(0, index=X.index)
        new_col[X[col] == val] = 1
        X[str(val)] = new_col
    if deleteCol:
        X.drop(col, axis=1, inplace=True)


def setSeason(X, deleteMonth=True):
    """
    X: pandas DataFrame
    Creates a new feature: season. It can have four values (win, spr, sum, and aut) depending on the month
    """
    new_col = pd.Series(index=X.index)
    new_col[X['month'].isin(['dec', 'jan', 'feb'])] = 'win'
    new_col[X['month'].isin(['mar', 'apr', 'may'])] = 'spr'
    new_col[X['month'].isin(['jun', 'jul', 'aug'])] = 'sum'
    new_col[X['month'].isin(['sep', 'oct', 'nov'])] = 'aut'
    X['season'] = new_col
    if deleteMonth:
        X.drop('month', axis=1, inplace=True)


def dropConst(X):
    """
    X: pandas DataFrame
    Deletes the columns that have the same value for each input vector
    """
    for col in X.columns:
        if X[col].min() == X[col].max():
            X.drop(col, axis=1, inplace=True)



data = pd.read_csv('data/forestfires.csv') # Load the dataset

# Datasets for regression

X = data.copy()
X = X[X.area > 0] # We only take data where the fire has happened
y = X['area'] # Extract labels from the dataset
X.drop('area', axis=1, inplace=True) # Removing the label column from X
X.drop(['X', 'Y'], axis=1, inplace=True)

X1 = X.copy()
X2 = X.copy()
X3 = X.copy()

# First training set
setDays2(X)
setMonths2(X)
dropConst(X)
scaleData(X)

# Second training set
setWeekend(X1)
X1['hot'] = pd.Series(0, index=X1.index)
X1.loc[X1.month.isin(['may', 'jun', 'jul', 'aug', 'sep']), 'hot'] = 1
X1.drop('month', axis=1, inplace=True)
scaleData(X1)
dropConst(X1)

# Third training set
setSeason(X2)
setCategorical(X2, 'season')
setWeekend(X2)
scaleData(X2)
dropConst(X2)

# Fourth training set
X3.drop(['day', 'month', 'FFMC', 'DMC', 'DC', 'ISI'], axis=1, inplace=True)
scaleData(X3)



# Datasets for classification

C = data.copy()
c = C['area'] # Extract labels from the dataset
c[C.area > 0] = 1
C.drop('area', axis=1, inplace=True) # Removing the label column from C
C.drop(['X', 'Y'], axis=1, inplace=True)

C1 = C.copy()
C2 = C.copy()
C3 = C.copy()

# First training set
setDays2(C)
setMonths2(C)
dropConst(C)
scaleData(C)

# Second training set
setWeekend(C1)
C1['hot'] = pd.Series(0, index=C1.index)
C1.loc[C1.month.isin(['may', 'jun', 'jul', 'aug', 'sep']), 'hot'] = 1
C1.drop('month', axis=1, inplace=True)
scaleData(C1)
dropConst(C1)

# Third training set
setSeason(C2)
setCategorical(C2, 'season')
setWeekend(C2)
scaleData(C2)
dropConst(C2)

# Fourth training set
C3.drop(['day', 'month', 'FFMC', 'DMC', 'DC', 'ISI'], axis=1, inplace=True)
scaleData(C3)


