package Persistence.Entities.activity

import Persistence.annotations.Column
import Persistence.annotations.ColumnTypes
import Persistence.model.Model

class ActivityModel(
    @field:Column(type = ColumnTypes.INTEGER) var id_activity: Int?,
    @field:Column(type = ColumnTypes.INTEGER) var id_act_category: Int?,
    @field:Column(type = ColumnTypes.INTEGER) var id_course_module: Int?,
    @field:Column(type = ColumnTypes.INTEGER) var posted_by: Int?,
    @field:Column(type = ColumnTypes.INTEGER) var day_week: Int?,
    @field:Column(type = ColumnTypes.TEXT) var act_starttime: String?,
    @field:Column(type = ColumnTypes.TEXT) var act_endtime: String?,
    @field:Column(type = ColumnTypes.INTEGER) var term: Int?,

    ): Model("activities") {

}