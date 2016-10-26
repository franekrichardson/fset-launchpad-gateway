package uk.gov.hmrc.fsetlaunchpadgateway.controllers

import org.scalatestplus.play.OneAppPerSuite
import play.api.libs.json.{ JsValue, Json }
import play.api.mvc.{ AnyContentAsEmpty, AnyContentAsJson }
import play.api.test.Helpers._
import org.mockito.Matchers.any
import org.mockito.Mockito._
import play.api.test.{ FakeHeaders, FakeRequest, Helpers }
import uk.gov.hmrc.fsetlaunchpadgateway.connectors.faststream.FaststreamClient
import uk.gov.hmrc.fsetlaunchpadgateway.connectors.faststream.exchangeobjects._
import uk.gov.hmrc.play.http.HeaderCarrier

import scala.concurrent.Future

class CallbackControllerSpec extends BaseControllerSpec with OneAppPerSuite {
  "Callback Controller#present" should {
    "correctly parse and reply to View Branded Video Callbacks" in new TestFixture {
      val result = controller.present()(makeCallbackJsonPostRequest(viewBrandedVideoJson))

      status(result) mustBe OK

      verify(mockFaststreamClient, times(1)).viewBrandedVideoCallback(any[ViewBrandedVideoCallbackRequest]())(any[HeaderCarrier]())
    }

    "correctly parse and reply to Setup Process Callbacks" in new TestFixture {
      val result = controller.present()(makeCallbackJsonPostRequest(setupProcessJson))

      status(result) mustBe OK

      verify(mockFaststreamClient, times(1)).setupProcessCallback(any[SetupProcessCallbackRequest]())(any[HeaderCarrier]())
    }

    "correctly parse and reply to View Practice Question Callbacks" in new TestFixture {
      val result = controller.present()(makeCallbackJsonPostRequest(viewPracticeQuestionJson))

      status(result) mustBe OK

      verify(mockFaststreamClient, times(1)).viewPracticeQuestionCallback(any[ViewPracticeQuestionCallbackRequest]())(any[HeaderCarrier]())
    }

    "correctly parse and reply to Question Callbacks" in new TestFixture {
      val result = controller.present()(makeCallbackJsonPostRequest(questionCallbackJson))

      status(result) mustBe OK

      verify(mockFaststreamClient, times(1)).questionCallback(any[QuestionCallbackRequest]())(any[HeaderCarrier]())
    }

    "correctly parse and reply to Final Callbacks" in new TestFixture {
      val result = controller.present()(makeCallbackJsonPostRequest(finalCallbackJson))

      status(result) mustBe OK

      verify(mockFaststreamClient, times(1)).finalCallback(any[FinalCallbackRequest]())(any[HeaderCarrier]())
    }

    "correctly parse and reply to Finished Callbacks" in new TestFixture {
      val result = controller.present()(makeCallbackJsonPostRequest(finishedCallbackJson))

      status(result) mustBe OK

      verify(mockFaststreamClient, times(1)).finishedCallback(any[FinishedCallbackRequest]())(any[HeaderCarrier]())
    }

    "when parsing valid but unrecognisable json, but with a valid status key, return a bad request" in new TestFixture {
      val result = controller.present()(makeCallbackJsonPostRequest(unrecogniseableJsonValidStatus))
    }

    "when parsing valid but unrecognisable json, return a bad request" in new TestFixture {
      val result = controller.present()(makeCallbackJsonPostRequest(unrecogniseableJson))
    }

    "when parsing invalid json, return a bad request" in new TestFixture {
      val result = controller.present()(makeCallbackPostRequest(invalidJson))
    }
  }

  trait TestFixture {

    val mockFaststreamClient = mock[FaststreamClient]

    when(mockFaststreamClient.viewBrandedVideoCallback(any())(any())).thenReturn(Future.successful(()))
    when(mockFaststreamClient.setupProcessCallback(any())(any())).thenReturn(Future.successful(()))
    when(mockFaststreamClient.viewPracticeQuestionCallback(any())(any())).thenReturn(Future.successful(()))
    when(mockFaststreamClient.questionCallback(any())(any())).thenReturn(Future.successful(()))
    when(mockFaststreamClient.finishedCallback(any())(any())).thenReturn(Future.successful(()))
    when(mockFaststreamClient.finalCallback(any())(any())).thenReturn(Future.successful(()))

    class TestController() extends CallbackController(mockFaststreamClient)

    val controller = new TestController()

    def makeCallbackJsonPostRequest(json: String): FakeRequest[AnyContentAsJson] = FakeRequest(Helpers.POST, "/faststream/callback")
      .withJsonBody(Json.parse(json))

    def makeCallbackPostRequest(content: String): FakeRequest[String] = FakeRequest(Helpers.POST, "/faststream/callback")
      .withBody(content)

    val viewBrandedVideoJson =
      s"""
       | {
       | "candidate_id":"cnd_222670c903da0bf709660ec3129ccdf6",
       | "custom_candidate_id":"FSCND-7292eea2-57f0-4d25-afc7-fd605c9388f9",
       | "interview_id":13917,
       | "custom_interview_id":null,
       | "custom_invite_id":"FSINV-2d442182-c7f4-4380-9187-8b9d8b852a19",
       | "status":"view_branded_video",
       | "deadline":"2016-10-28"
       | }
       """.stripMargin

    val setupProcessJson =
      s"""
         | {
         | "candidate_id":"cnd_222670c903da0bf709660ec3129ccdf6",
         | "custom_candidate_id":"FSCND-7292eea2-57f0-4d25-afc7-fd605c9388f9",
         | "interview_id":13917,
         | "custom_interview_id":null,
         | "custom_invite_id":"FSINV-2d442182-c7f4-4380-9187-8b9d8b852a19",
         | "status":"setup_process",
         | "deadline":"2016-10-28"
         | }
       """.stripMargin

    val viewPracticeQuestionJson =
      s"""
         | {
         | "candidate_id":"cnd_222670c903da0bf709660ec3129ccdf6",
         | "custom_candidate_id":"FSCND-7292eea2-57f0-4d25-afc7-fd605c9388f9",
         | "interview_id":13917,
         | "custom_interview_id":null,
         | "custom_invite_id":"FSINV-2d442182-c7f4-4380-9187-8b9d8b852a19",
         | "status":"view_practice_question",
         | "deadline":"2016-10-28"
         | }
       """.stripMargin

    val questionCallbackJson =
      s"""
         | {
         | "candidate_id":"cnd_222670c903da0bf709660ec3129ccdf6",
         | "custom_candidate_id":"FSCND-7292eea2-57f0-4d25-afc7-fd605c9388f9",
         | "interview_id":13917,
         | "custom_interview_id":null,
         | "custom_invite_id":"FSINV-2d442182-c7f4-4380-9187-8b9d8b852a19",
         | "status":"question",
         | "deadline":"2016-10-28",
         | "question_number":"2"
         |}
       """.stripMargin

    val finalCallbackJson =
      s"""
         | {
         | "candidate_id":"cnd_222670c903da0bf709660ec3129ccdf6",
         | "custom_candidate_id":"FSCND-7292eea2-57f0-4d25-afc7-fd605c9388f9",
         | "interview_id":13917,
         | "custom_interview_id":null,
         | "custom_invite_id":"FSINV-2d442182-c7f4-4380-9187-8b9d8b852a19",
         | "status":"final",
         | "deadline":"2016-10-28"
         | }
       """.stripMargin

    val finishedCallbackJson =
      s"""
         | {
         | "candidate_id":"cnd_222670c903da0bf709660ec3129ccdf6",
         | "custom_candidate_id":"FSCND-7292eea2-57f0-4d25-afc7-fd605c9388f9",
         | "interview_id":13917,
         | "custom_interview_id":null,
         | "custom_invite_id":"FSINV-2d442182-c7f4-4380-9187-8b9d8b852a19",
         | "status":"finished",
         | "deadline":"2016-10-28"
         | }
       """.stripMargin

    // TODO: Verify this with live, parser is complex
    val reviewedCallbackJson =
      s"""
         |{
         |    'candidate_id':                 'cnd_1234567890682519d5c44e940254cc61',
         |    'custom_candidate_id':          'FSCND-7292eea2-57f0-4d25-afc7-fd605c9388f9',
         |    'interview_id':                 '13917',
         |    'custom_interview_id':           null,
         |    'custom_invite_id':             'FSINV-2d442182-c7f4-4380-9187-8b9d8b852a19',
         |    'status':                       'reviewed',
         |    'deadline':                     '2016-10-28',
         |    'reviews':  {
         |        'total_average':  {
         |            'type':                 'video_interview',
         |            'score_text':           '80%',
         |            'score_value':          '80'
         |        },
         |        'reviewers':    {
         |            'reviewer_1':    {
         |                'name':             'John Doe',
         |                'email':            'reviewer_1@email.com',
         |                'comment':          'my comments',
         |                'review_criteria_1':    {
         |                    'text':         'Can you see this API question?',
         |                    'type':         'yes_no',
         |                    'score':        'yes'
         |                },
         |                'review_criteria_2':    {
         |                    'text':         'Rate from one to five.',
         |                    'type':         'numeric',
         |                    'score':        '3'
         |                }
         |            },
         |            'reviewer_2':    {
         |                'name':             'Jane Doe',
         |                'email'             'reviewer_2@email.com',
         |                'comment':          'another set of comments',
         |                'question_1':    {
         |                    'text':         'Why should we hire you?',
         |                    'id':           '1234',
         |                    'review_criteria_1':    {
         |                        'text':     'Can you see this API question?',
         |                        'type':     'yes_no',
         |                        'score':    'no'
         |                    },
         |                    'review_criteria_2':    {
         |                        'text':     'Rate from one to five.',
         |                        'type':     'numeric',
         |                        'score':    '5'
         |                    }
         |                }
         |            }
         |        }
         |    }
         |}
       """.stripMargin

    val unrecogniseableJsonValidStatus =
      s"""
         | {
         |   "status": "final"
         | }
       """.stripMargin

    val unrecogniseableJson =
      s"""
         | {
         |   "key": "foo"
         | }
       """.stripMargin

    val invalidJson =
      s"""
         | Not valid Json
       """.stripMargin
  }
}
