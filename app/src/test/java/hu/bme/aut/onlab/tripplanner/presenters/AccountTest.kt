package hu.bme.aut.onlab.tripplanner.presenters

import android.content.Context
import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.onlab.tripplanner.data.disk.model.User
import hu.bme.aut.onlab.tripplanner.domain.AuthInteractor
import hu.bme.aut.onlab.tripplanner.domain.UserInteractor
import hu.bme.aut.onlab.tripplanner.ui.list.pages.account.AccountPresenter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AccountPresenterTest {

    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(scheduler)

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        authInteractor = mockk()
        userInteractor = mockk()
        presenter = AccountPresenter(authInteractor, userInteractor)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }


    private lateinit var authInteractor: AuthInteractor
    private lateinit var userInteractor: UserInteractor
    private lateinit var presenter: AccountPresenter

    private val context: Context = mockk(relaxed = true)

    @Test
    fun `getUserEmail delegates to AuthInteractor`() = runTest {
        coEvery { authInteractor.getCurrentUserEmail() } returns "test@example.com"

        val result = presenter.getUserEmail()

        assertThat(result).isEqualTo("test@example.com")
        coVerify(exactly = 1) { authInteractor.getCurrentUserEmail() }
    }

    @Test
    fun `signOut delegates to AuthInteractor`() = runTest {
        coEvery { authInteractor.signOut() } returns Unit

        presenter.signOut()

        coVerify(exactly = 1) { authInteractor.signOut() }
    }

    @Test
    fun `changePassword delegates to AuthInteractor`() = runTest {
        coEvery { authInteractor.sendPasswordReset() } returns Unit

        presenter.changePassword()

        coVerify(exactly = 1) { authInteractor.sendPasswordReset() }
    }

    @Test
    fun `changeEmail delegates to AuthInteractor`() = runTest {
        coEvery { authInteractor.changeEmail(context, "pass", "new@mail.com") } returns Unit

        presenter.changeEmail(context, "pass", "new@mail.com")

        coVerify(exactly = 1) { authInteractor.changeEmail(context, "pass", "new@mail.com") }
    }

    @Test
    fun `getUser delegates to UserInteractor`() = runTest {
        val mockUser = User(email = "user@mail.com")
        coEvery { userInteractor.getCurrentUser() } returns mockUser

        val result = presenter.getUser()

        assertThat(result).isEqualTo(mockUser)
        coVerify(exactly = 1) { userInteractor.getCurrentUser() }
    }
}
