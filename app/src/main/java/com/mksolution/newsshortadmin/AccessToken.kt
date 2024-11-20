package com.mksolution.newsshortadmin

import com.google.auth.oauth2.GoogleCredentials
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

object AccessToken {
    private val firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging"
    fun getAccessToken() : String?{

        try {
            val jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"news-short-11dd4\",\n" +
                    "  \"private_key_id\": \"9f364641ae20e3498773399a04a63f218c62cbdb\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDbOD5ul+pR4KVO\\nWSq61iHSB3dteOnYUhj0GuS9tA+ys5DU+lcusB26KFJIYNFCvOQvm3dS1dGY8NWM\\ne63GNnEuKRXpn2O6nCareMBIvB1S3b9zBd5QDnfrHCfH2BJdjkrA4acJ8q8WQEoU\\nCWrpAnPO9SBoZxdspCAovJB+CL8YrCK100AxT/LererQa7GcJ4L3qfIoYOrw4YdN\\n1z7Ok8jpVAtIlf/WdPf8sLPvLaMWERquAc4MlU+5gg0hWwEiLNyHZSD2aiaiRNp/\\nraOCPLrnZ566LBR4vJBQM/vgzC6q6ghvSjfp00o4wU7vGO/8POw4jkOBKnhLnh1H\\nnHrq/dgtAgMBAAECggEACuJyBx+33LTgwFEhFAHJ0Bnazi6wvbLl1kj3jovAM8lC\\nqbB8X68S6slRvJJCEaQ7WDnJ5O0E6SkcEUuaxzqwx+/2TJFDbGKQcPcf5PyiIBku\\nhlGGRkdRoPTW7M9HqimEgKszlgTcuKgchX4qNsOOaHo3T7e+rABCMIOcUQWyJ1Fd\\nI93yXfO1WifkOK6N3arw50+lw3CKJM3o+XMcN/mbUry8I2bZ/fV0jcYB8xkDvvxl\\nh4MIghcc/lTEoD967jH3WuGEy0deXiy0O7vQInLNb7lzWJ20CtH7v6iRbgVx9S0K\\nPr8HTH+rFADLeh/5Uh6DnWPo1dgo4MfdftKyKb7EaQKBgQD3VgcxcWGN2WkZ5EaT\\nNPkFYG6YZhvItIF7qdy8wOTBizffpEUehm2JgNv+CeYI/1jOQ7wFwygx45Ff7RDV\\nuZyoP2s1pIrAMdFhXW8AONmWoOt3ddBXJsce6IHA2R4lFXwjdKxo1vg54sgHLH4s\\nD/Ccu0MQ4d9HfTgUr7yeUso+8wKBgQDi5hWE7HQxQ8tQ9Lzp1Rol7gr2EvbPDVMp\\nHUjkTv7bmURhwfnqiD6eiNiqeta4JD2HbTfG8I5jbQKJ4QvEsBWrAoS5EGeH/VkB\\n1PwXYvX0Fhw/BVZ+0CWz/XarK6SZMM61AXB9curU4qZmhwOZv096c7uueNLD2YL2\\nmg/uf0SUXwKBgCz4i15GC0LLYPzAMMTbQNvlaLZ0+mBH0bM2c1guimlWctZK+/ZM\\n4uW5dEaAbj78jp0SW8GAdnJ7uhcC96ikkwNUZJ3gSQohBLz7TA9yRr3IjIev+C2g\\nc1v/UnhKAd0kRun7cM2vKHr97PjA8j/4tfsJYWxUZVCOSAAa4+S1kOhZAoGBAKTv\\nzETL/cqyFWHMBmeUWWWpHJ60OmVYslL4zrvoEKthRqwpbZpnX92ubJHAqcVB0thX\\nNGyRAXCVFXtU9FEODzI4qnI80oWwTLtgL8BwV4oPaIkHQFD+zemBHnpfR090g05g\\ntXz7xGjsOTnee2eQAh5pcszpV4y22NSaOWqNaTTRAoGALZukLpJSq+lGxjlPcUMY\\nQS3EkJ67IcX1uSRndWD2z6NE5R/o4YHQTGrI1tWf/FqtgYSAPbuNCNOnijyqNjPU\\nZqJ717B5BR/hTBqQU8+yHlDz+AJKI9SaTsjO3d5/5vTrBNHT3u761L3dvz082ZW1\\nvIL5cshBjDwHhouSDQIqj7U=\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-8e3on@news-short-11dd4.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"106344874425440202823\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-8e3on%40news-short-11dd4.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}"
            val stream = ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))
            val googleCredential = GoogleCredentials.fromStream(stream)
                .createScoped(arrayListOf(firebaseMessagingScope))
            googleCredential.refresh()
            return googleCredential.accessToken.tokenValue
        }catch (e: IOException){
            return null
        }
    }
}