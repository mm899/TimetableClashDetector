package Persistence.Entities.course

import java.sql.ResultSet
import Persistence.ResultSetToModel

class ResultSetToCourse : ResultSetToModel<CourseModel>() {
    override fun rsToModel(rs: ResultSet) : CourseModel {
        return CourseModel(
            id_course = rs.getInt("id_course"),
            id_course_type=rs.getInt("id_course_type"),
            start_year = rs.getInt("start_year"),
            end_year = rs.getInt("end_year"),
            name = rs.getString("name")
        )
    }
}