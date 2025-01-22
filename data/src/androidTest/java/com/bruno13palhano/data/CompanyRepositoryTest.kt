package com.bruno13palhano.data

import com.bruno13palhano.data.datasource.local.dao.CompanyDao
import com.bruno13palhano.data.datasource.local.database.AppDatabase
import com.bruno13palhano.data.datasource.local.entity.CompanyEntity
import com.bruno13palhano.data.model.shared.Address
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import java.io.IOException
import javax.inject.Inject
import kotlin.jvm.Throws
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
internal class CompanyRepositoryTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: AppDatabase
    private lateinit var companyDao: CompanyDao

    @Before
    fun setup() {
        hiltRule.inject()
        companyDao = database.companyDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun shouldReturnTheCompanyWithTheUid() = runTest {
        val company = CompanyEntity(
            uid = "123",
            name = "company",
            email = "email",
            password = "password",
            phone = "phone",
            address = Address("", "", "", ""),
            sellers = emptyList(),
            socialMedia = emptyList(),
            lastModifiedTimestamp = "",
        )

        companyDao.insert(company = company)

        val result = companyDao.getCompany(uid = company.uid)

        assertThat(result).isEqualTo(company)
    }

    @Test
    fun should() = runTest {
        val company = CompanyEntity(
            uid = "123",
            name = "company",
            email = "email",
            password = "password",
            phone = "phone",
            address = Address("", "", "", ""),
            sellers = emptyList(),
            socialMedia = emptyList(),
            lastModifiedTimestamp = "",
        )
    }
}
