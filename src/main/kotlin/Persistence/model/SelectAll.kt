package Persistence.model

import Persistence.DBConnection.DBConnector
import Persistence.ResultSetToModel

class SelectAll<T: Model>(val dbConnector: DBConnector, val model: T, val rsToModel: ResultSetToModel<T>) {
    fun select() : ArrayList<T>{
        return dbConnector.rawSelectWithModel<T>(
            "SELECT * FROM ${model.tableName}",
            rsToModel
        )
    }
}