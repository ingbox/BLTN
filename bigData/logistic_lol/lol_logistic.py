import pandas as pd
import numpy as np
from sklearn.linear_model import LogisticRegression
from sklearn.linear_model import LinearRegression
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, r2_score

lol_df = pd.read_csv('csv_files/lol/Challenger_Ranked_Games_15minute.csv')
lol_df = lol_df.iloc[:,1:47]

Y = lol_df['blueWins']
X = lol_df.drop(['blueWins','redWins'], axis= 1, inplace= False)

X = StandardScaler().fit_transform(X) # 값이 너무 차이가 커서 한번 정규 분포로 스케일링

X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size = 0.3, random_state = 500)

lr = LinearRegression()
lor = LogisticRegression(max_iter= 1000)
lr.fit(X_train, Y_train)
lor.fit(X_train, Y_train)

Y_predict_l = lr.predict(X_test)
Y_predict_lo = lor.predict(X_test)

mse = mean_squared_error(Y_test, Y_predict_lo)
rmse = np.sqrt(mse)
print('MSE : {0:.3f}, RMSE : {1:.3f}'.format(mse, rmse))
print('R^2(Variance score) : {0:.3f}'.format(r2_score(Y_test, Y_predict_lo)))

print('Y 절편 값: ', lr.intercept_)
print(lol_df.info())
print('회귀 계수 값: {0:.3f}', np.round(lr.coef_,3))
