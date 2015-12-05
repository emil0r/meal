(ns meal.dev
  (:require [meal.init :as init]))


(comment
  (do
    (init/stop)
    (init/init "settings.edn"))
  )
