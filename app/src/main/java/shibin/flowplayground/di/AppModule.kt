package shibin.flowplayground.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import shibin.flowplayground.di.repository.FlowOperatorRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFlowOperatorRepository(): FlowOperatorRepository =
        FlowOperatorRepository()
}