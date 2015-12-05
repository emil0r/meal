(ns meal.channel
  (:require [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer [sente-web-server-adapter]]))

;; setup sente
(let [{:keys [ch-recv send-fn ajax-post-fn ajax-get-or-ws-handshake-fn
              connected-uids]}
      (sente/make-channel-socket! sente-web-server-adapter)]
  (def ring-ajax-post ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk ch-recv) ; ChannelSocket's receive channel
  (def chsk-send! send-fn) ; ChannelSocket's send API fn
  (def connected-uids connected-uids) ; Watchable, read-only atom
  )


(defmulti handle-event)

;; (chsk-send! ;;"9ba13adb-bc74-4719-92c2-b5e159ce268e"
;;  ;;:taoensso.sente/nil-uid
;;  ;;:sente/all-users-without-uid
;;  (first (:ws @connected-uids))
;;  [:foo/bar {:test (rand 1000)}]
;;             1000
;;             (fn [& args]
;;               #spy/d args))

;;@connected-uids
