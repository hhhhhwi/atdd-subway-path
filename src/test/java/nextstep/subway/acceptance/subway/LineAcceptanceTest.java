package nextstep.subway.acceptance.subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.subway.LineSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static nextstep.subway.utils.subway.LineSteps.노선_생성_요청;
import static nextstep.subway.utils.subway.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
	private static final String TEST_LINE_NAME_1 = "신분당선";
	private static final String TEST_LINE_NAME_2 = "분당선";
	private static final String TEST_LINE_COLOR_1 = "bg-red";
	private static final String TEST_LINE_COLOR_2 = "bg-blue";

	/**
	 * When 지하철 노선을 생성하면
	 * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
	 */
	@DisplayName("지하철 노선을 생성한다.")
	@Test
	void createLineTest() {
		// when
		ExtractableResponse<Response> response = 노선_생성_요청(TEST_LINE_NAME_1, TEST_LINE_COLOR_1, 종로3가역, 시청역, 10);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(LineSteps.노선_전체_조회_요청().jsonPath().getList("name")).contains(TEST_LINE_NAME_1);
	}

	/**
	 * Given 2개의 지하철 노선을 생성하고
	 * When 지하철 노선 목록을 조회하면
	 * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
	 */
	@DisplayName("지하철 노선 목록을 조회한다.")
	@Test
	void getLinesTest() {
		// given
		노선_생성_요청(TEST_LINE_NAME_1, TEST_LINE_COLOR_1, 종로3가역, 시청역, 10);
		노선_생성_요청(TEST_LINE_NAME_2, TEST_LINE_COLOR_2, 동대문역, 종로5가역, 10);

		// when
		ExtractableResponse<Response> response = LineSteps.노선_전체_조회_요청();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getList("name")).contains(TEST_LINE_NAME_1, TEST_LINE_NAME_2);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 조회하면
	 * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
	 */
	@DisplayName("지하철 노선을 조회한다.")
	@Test
	void getLineTest() {
		// given
		Long id = 노선_생성_요청(TEST_LINE_NAME_1, TEST_LINE_COLOR_1, 종로3가역, 시청역, 10).jsonPath().getLong("id");

		// when
		ExtractableResponse<Response> response = LineSteps.노선_단건_조회_요청(id);

		// then
		assertThat(response.jsonPath().getString("name")).isEqualTo(TEST_LINE_NAME_1);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 수정하면
	 * Then 해당 지하철 노선 정보는 수정된다
	 */
	@DisplayName("지하철 노선을 수정한다.")
	@Test
	void updateLineTest() {
		// given
		Long id = 노선_생성_요청(TEST_LINE_NAME_1, TEST_LINE_COLOR_1, 종로3가역, 시청역, 10).jsonPath().getLong("id");

		// when
		ExtractableResponse<Response> response = LineSteps.노선_수정_요청(TEST_LINE_NAME_2, TEST_LINE_COLOR_2, id);
		ExtractableResponse<Response> getResponse = LineSteps.노선_단건_조회_요청(id);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(getResponse.jsonPath().getString("name")).isEqualTo(TEST_LINE_NAME_2);
		assertThat(getResponse.jsonPath().getString("color")).isEqualTo(TEST_LINE_COLOR_2);
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 빈 값이 있는 채로 수정하면
	 * Then 해당 지하철 노선 정보는 수정되지 않고 Bad Request (400) 을 반환한다.
	 */
	@DisplayName("지하철 노선을 수정한다.")
	@Test
	void updateLineWithNullThenFailTest() {
		// given
		Long id = 노선_생성_요청(TEST_LINE_NAME_1, TEST_LINE_COLOR_1, 종로3가역, 시청역, 10).jsonPath().getLong("id");

		// when
		ExtractableResponse<Response> response = LineSteps.노선_수정_요청(TEST_LINE_NAME_2, "", id);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	/**
	 * Given 지하철 노선을 생성하고
	 * When 생성한 지하철 노선을 삭제하면
	 * Then 해당 지하철 노선 정보는 삭제된다
	 */
	@DisplayName("지하철 노선을 삭제한다.")
	@Test
	void deleteLineTest() {
		// given
		Long id = 노선_생성_요청(TEST_LINE_NAME_1, TEST_LINE_COLOR_1, 종로3가역, 시청역, 10).jsonPath().getLong("id");

		// when
		ExtractableResponse<Response> response = LineSteps.노선_삭제_요청(id);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
		assertThat(LineSteps.노선_전체_조회_요청().jsonPath().getList("id")).doesNotContain(id);
	}
}
