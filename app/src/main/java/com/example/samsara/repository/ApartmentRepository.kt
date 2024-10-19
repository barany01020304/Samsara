import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.datasource.remote.RetrofitConfig

class ApartmentRepository {
    suspend fun getApartments(): List<ApartmentDataModel> {
        return RetrofitConfig.retrofitCreate().getApartments()
    }
}