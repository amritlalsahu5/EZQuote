# EZQuote
EZQuote Stock Market Android Application

Description
-----------
An Android application which provides an overview of the stock market, as well as the individual stocks that has been 
selected by the user. The app can display all related news of the stock, the parameters, and history prices of the stock.
It also has a background service that notifies the user once the targeted price has been reached. The app is perfect for 
people who are new to the stock market, as well as people who have certain experiences.

## Features
+ Register and login with email address and password
+ Display the symbol, price, and price change of the stocks, which are in the user’s watch list, in a scroll list.
+ Display the market news. If the user clicks on any stock in the list, the related news of the specific stock will 
be displayed. If the user clicks on any news, the user will be brought to the news through browser.
+ By scrolling the stock item leftward, the user will be able to view the detail of the stock, or remove the stock 
from the watch list.
+ After click the search button, a user will be able to search a stock using the stock symbol. The user will be brought 
to the detail view of the stock.
+ The upper half of the stock’s detailed view is a graph, which shows the price and volume during certain period. 
User can select 1 month, 3 months, 6 months, 1 year, and 3 years as the period of the graph. The graph support 
pinch to zoom function. *Note that for certain legal reasons, Yahoo Finance API does not necessarily provide the 
whole history of some stocks.
+ The lower half of the stock’s detailed view shows all the parameters of a stock. The names of the parameter are 
clickable, and an alert dialog will pop up to give an explanation of the parameter.
+ The bottom has two buttons. User can click Favorites to add the stock to the watch list, or click to remove. User
can click the Alert me to set alert for the specific stock.
+ The alert me function can alert the user once the stock price increase to a certain value, or decrease to a certain 
value. Once the value has been reached, a notification will pop up. 
+ The menu contains two functions. One is logout, user can click to logout anytime. If the user re-login, his/her watch 
list and alerts will still be exist. The other function is manage alerts. User can delete any unwanted alerts through 
this menu.

## Implementation
+ Yahoo Finance API - to retrieve stock parameters, news and history price.
+ Parse.com - for data repository and to store user authentication, stock watch list and alerts.
+ MPAndroidCharts - has been used to graph the history price and trade volume.
+ Fragments

