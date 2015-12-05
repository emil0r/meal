(ns meal.views.index
  (:require [hiccup.page :refer [html5 include-css]]
            [ring.util.http-response :refer [ok]]))


(defn index [request]
  (ok (html5
       [:head
        [:meta {:charset "UTF-8"}]
        [:title "Share a meal with me!"]
        [:link {:rel "icon" :type "image/png" :href "/static/img/meal.png"}]
        (map include-css ["/static/css/main.css"])]

       [:body
        [:noscript "You need javascript enabled"]
        [:div#app {:style "height: 100%;"}]
        [:script {:type "text/javascript"
                  :src "/js/compiled/meal.js"}]])))
