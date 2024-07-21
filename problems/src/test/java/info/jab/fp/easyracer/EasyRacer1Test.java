package info.jab.fp.easyracer;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

/**
 * Race 2 concurrent requests
 * GET /1
 * The winner returns a 200 response with a body containing right
 */

@WireMockTest(httpPort = 8090)
class EasyRacer1Test {
    
    @Test
    void should_work(WireMockRuntimeInfo wmRuntimeInfo) {
        //Given
        // Stub 1 (loser): respond with any status code except 200
        stubFor(get(urlEqualTo("/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("right")));

        // Stub 2 (winner): respond with 200 and "right" body
        stubFor(get(urlEqualTo("/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("right")));

        var address = "http://localhost:8090/1";

        //When
        EasyRacer1 easyracer = new EasyRacer1();
        var result = easyracer.call(address);

        //Then
        assertThat(result).isEqualTo("right");
    }

}
