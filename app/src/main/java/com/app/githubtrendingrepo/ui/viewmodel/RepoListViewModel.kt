import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.githubtrendingrepo.model.RepositoryResponse
import com.app.githubtrendingrepo.network.GithubRepository
import com.app.githubtrendingrepo.network.ReposParam
import com.app.githubtrendingrepo.network.Resource
import com.app.githubtrendingrepo.network.ServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.io.IOException


class RepoListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GithubRepository
    private val compositeDisposable = CompositeDisposable()
    private var githubRepoApiClient: ServiceGenerator

    private val repoItemList: MutableLiveData<Resource<List<RepositoryResponse.Item>>> =
        MutableLiveData()

    val repositoryItemList: LiveData<Resource<List<RepositoryResponse.Item>>> get() = repoItemList

    init {
        githubRepoApiClient = ServiceGenerator(application.applicationContext)
        repository = GithubRepository(githubRepoApiClient)
    }

    fun getRepos(reposParam: ReposParam,requestType:Boolean) {
        if (requestType){
            githubRepoApiClient.forceRequest(requestType)
        }
        repoItemList.value = Resource.loading()
        compositeDisposable.add(
            repository.getTrendingReposFromApi(reposParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)
        )
    }

    private val observer: DisposableSingleObserver<RepositoryResponse>
        get() = object : DisposableSingleObserver<RepositoryResponse>() {
            override fun onSuccess(t: RepositoryResponse) {
                repoItemList.value = Resource.success(t.items)
            }

            override fun onError(e: Throwable) {
                if (e is IOException) repoItemList.value = Resource.networkError()
                else repoItemList.value = Resource.error("No data found", null)
            }
        }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}