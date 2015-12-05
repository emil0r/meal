(ns meal.routes
  (:require [compojure.core :refer [defroutes GET POST]]
            [meal.channel :refer [ring-ajax-get-or-ws-handshake
                                   ring-ajax-post]]
            [meal.views.index :refer [index]]))



(defroutes app-routes
  (GET "/" [] index)

  (GET "/ch" request (ring-ajax-get-or-ws-handshake request))
  (POST "/ch" request (ring-ajax-post request)))
