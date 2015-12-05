(ns meal.core
  (:require [meal.init :as init])
  (:gen-class))



(defn -main [& args]
  (init/init "settings.edn"))
