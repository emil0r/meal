(ns meal.database
  (:require [clojure.java.jdbc :as jdbc]
            [com.stuartsierra.component :as component]
            [ez-database.core :as db]
            [joplin.core :as joplin]
            joplin.jdbc.database
            [taoensso.timbre :as log])
  (:import [com.jolbox.bonecp BoneCPDataSource]
           [ez_database.core EzDatabase]))


(defonce my-db (atom nil))

(defn query [& args]
  (apply db/query @my-db args))

(defn query! [& args]
  (apply db/query! @my-db args))

(defn query<! [& args]
  (apply db/query<! @my-db args))


(extend-protocol jdbc/IResultSetReadColumn
  org.postgresql.jdbc4.Jdbc4Array
  (result-set-read-column [pgobj metadata i]
    (vec (.getArray pgobj))))

(defn- bonecp-datasource
  "BoneCP based connection pool"
  [db-spec datasource]
  (let [{:keys [subprotocol subname user password]} db-spec
        {:keys [connection-timeout
                default-autocommit
                maxconns-per-partition
                minconns-per-partition
                partition-count]
         :or {connection-timeout 2000
              default-autocommit false
              maxconns-per-partition 10
              minconns-per-partition 5
              partition-count 1}} datasource
         ds (doto (BoneCPDataSource.)
              (.setJdbcUrl (str "jdbc:" subprotocol ":" subname))
              (.setUsername user)
              (.setPassword password)
              (.setConnectionTestStatement "select 42;")
              (.setConnectionTimeoutInMs connection-timeout)
              (.setDefaultAutoCommit default-autocommit)
              (.setMaxConnectionsPerPartition maxconns-per-partition)
              (.setMinConnectionsPerPartition minconns-per-partition)
              (.setPartitionCount partition-count))]
    (assoc db-spec :datasource ds)))

(extend-type EzDatabase
  component/Lifecycle
  (start [this]
    (let [{:keys [db-specs ds-specs]} this]
      (if (get-in db-specs [:default :datasource])
        this
        (do
          (log/info "Starting database")
          (let [db-specs (into
                          {}
                          (map
                           (fn [key]
                             (let [db-spec (get db-specs key)
                                   ds-spec (get ds-specs key)]
                               [key (bonecp-datasource db-spec ds-spec)]))
                           (keys db-specs)))
                this (assoc this :db-specs db-specs)]
            (reset! my-db this)
            this)))))
  (stop [this]
    (let [db-specs (:db-specs this)]
      (if-not (get-in db-specs [:default :datasource])
        this
        (do
          (log/info "Stopping database")
          (doseq [[key db-spec] db-specs]
            (.close (:datasource db-spec))
            (log/info "Closed datasource for" key))
          (let [this (assoc this :db-specs (into
                                            {} (map (fn [[key db-spec]]
                                                      [key (dissoc db-spec :datasource)])
                                                    db-specs)))]
            (reset! my-db nil)
            this))))))



(defn database [specs]
  (db/map->EzDatabase {:db-specs specs}))
