@Library('devops-library') _

// Edit your app's name below
def APP_NAME = 'scheduling-hub'
def PATHFINDER_URL = "pathfinder.gov.bc.ca"
def PROJECT_PREFIX = "jag-shuber"
// Edit your environment TAG names below
def TAG_NAMES = [
  'dev', 
  'test', 
  'prod'
]
def APP_URLS = [
  "https://${APP_NAME}-${PROJECT_PREFIX}-${TAG_NAMES[0]}.${PATHFINDER_URL}",
  "https://${APP_NAME}-${PROJECT_PREFIX}-${TAG_NAMES[1]}.${PATHFINDER_URL}",
  "https://${APP_NAME}-${PROJECT_PREFIX}-${TAG_NAMES[2]}.${PATHFINDER_URL}"
]

// You shouldn't have to edit these if you're following the conventions
def APP_BUILD = APP_NAME+'-build'
def IMAGESTREAM_NAME = APP_NAME
def SLACK_DEV_CHANNEL="#sheriffscheduling_dev"
def SLACK_MAIN_CHANNEL="#sheriff_scheduling"
def SLACK_PROD_CHANNEL="sheriff_prod_approval"
def work_space="/var/lib/jenkins/jobs/jag-shuber-tools-hub-pipeline/workspace@script"

stage('Build ' + APP_NAME) {
  node{
    // Cheking template exists  or else create
    openshift.withProject() {
      def templateSelector_APP_BUILD = openshift.selector( "bc/${APP_BUILD}" )
      def templateExists_APP_BUILD = templateSelector_APP_BUILD.exists()

      // If build config doesn't exists!! It will create one to avoid failure.
      if (!templateExists_APP_BUILD) { 
        APP_BUILD_IMG = sh ( """oc process -f "${work_space}/openshift/templates/scheduling-hub-build.json" | oc create -f - """)
        echo ">> ${APP_BUILD_IMG}"
      } else {
        echo "${APP_BUILD} Build exists"
      }
            
      try{
        echo "Building: " + APP_BUILD
        openshiftBuild bldCfg: APP_BUILD, showBuildLogs: 'true'
        // Verify APP_BUILD
        openshiftVerifyBuild bldCfg: APP_BUILD, showBuildLogs: 'true', waitTime: '900000'
                  
        // Don't tag with BUILD_ID so the pruner can do it's job; it won't delete tagged images.
        // Tag the images for deployment based on the image's hash
        IMAGE_HASH = sh (
        script: """oc get istag ${IMAGESTREAM_NAME}:latest | grep sha256: | awk -F "sha256:" '{print \$3 }'""",
        returnStdout: true).trim()
        echo ">> IMAGE_HASH: ${IMAGE_HASH}"

      }catch(error){
        echo "Error in Build"
        slackNotify(
          'Build Broken 🤕',
          "The latest ${APP_NAME} build seems to have broken\n'${error.message}'",
        'danger',
        env.SLACK_HOOK,
        SLACK_DEV_CHANNEL,
        [
          [
            type: "button",
            text: "View Build Logs",
            style:"danger",           
            url: "${currentBuild.absoluteUrl}/console"
          ]
        ])
        throw error
      }
    }
  }
}

// Deploying to Dev
stage('Deploy ' + TAG_NAMES[0]) {
  def environment = TAG_NAMES[0]
  def url = APP_URLS[0]
  node{
    try{
      openshiftTag destStream: IMAGESTREAM_NAME, verbose: 'true', destTag: environment, srcStream: IMAGESTREAM_NAME, srcTag: "${IMAGE_HASH}", waitTime: '900000'
      // verify deployment
      openshiftVerifyDeployment deploymentConfig: IMAGESTREAM_NAME, namespace: "${PROJECT_PREFIX}"+"-"+environment, waitTime: '900000'
      
      slackNotify(
          "New Version in ${environment} 🚀",
          "A new version of the ${APP_NAME} is now in ${environment}",
          'good',
          env.SLACK_HOOK,
          SLACK_MAIN_CHANNEL,
          [
            [
              type: "button",
              text: "View New Version",         
              url: "${url}"
            ],
            [
              type: "button",            
              text: "Deploy to Test?",
              style: "primary",              
              url: "${currentBuild.absoluteUrl}/input"
            ]
          ])
    }catch(error){
      slackNotify(
        "Couldn't deploy to ${environment} 🤕",
        "The latest deployment of the ${APP_NAME} to ${environment} seems to have failed\n'${error.message}'",
        'danger',
        env.SLACK_HOOK,
        SLACK_DEV_CHANNEL,
        [
          [
            type: "button",
            text: "View Build Logs",
            style:"danger",        
            url: "${currentBuild.absoluteUrl}/console"
          ]
        ])
      echo "Error in DEV"
    }
  }
}

//Deploying in stable Test
stage('Deploy ' + TAG_NAMES[1]){
  def environment = TAG_NAMES[1]
  def url = APP_URLS[1]
  timeout(time:3, unit: 'DAYS'){ input id: 'Approval', message: "Deploy to ${environment}?", submitter: 'ronald-garcia-admin,cjam-admin', submitterParameter: 'approvingSubmitter'}
  node{
    try{
      openshiftTag destStream: IMAGESTREAM_NAME, verbose: 'true', destTag: environment, srcStream: IMAGESTREAM_NAME, srcTag: "${IMAGE_HASH}", waitTime: '900000'
      // verify deployment
      openshiftVerifyDeployment deploymentConfig: IMAGESTREAM_NAME, namespace: "${PROJECT_PREFIX}"+"-"+environment, waitTime: '900000'
      slackNotify(
        "New Version in ${environment} 🚀",
        "A new version of the ${APP_NAME} is now in ${environment}",
        'good',
        env.SLACK_HOOK,
        SLACK_MAIN_CHANNEL,
          [
            [
              type: "button",
              text: "View New Version",           
              url: "${url}"
            ],
            [
              type: "button",            
              text: "Tag image production?",
              style: "primary",              
              url: "${currentBuild.absoluteUrl}/input"
            ]
          ])
    } catch(error){
      slackNotify(
        "Couldn't deploy to ${environment} 🤕",
        "The latest deployment of the ${APP_NAME} to ${environment} seems to have failed\n'${error.message}'",
        'danger',
        env.SLACK_HOOK,
        SLACK_DEV_CHANNEL,
        [
          [
            type: "button",
            text: "View Build Logs",
            style:"danger",        
            url: "${currentBuild.absoluteUrl}/console"
          ]
        ])
        echo "Build failed"
    }   
  }
}
