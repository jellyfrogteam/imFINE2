const localVideo = document.getElementById('localVideo')
localVideo.muted = true //html에서 태그자체에 muted 해줌

const defaultMediaConstraint = {
  video: true,
  audio: true
}

const backCameraMediaConstraint = {
  audio: true, 
  video: { facingMode: { exact: "environment" } }
}


navigator.mediaDevices.getUserMedia(defaultMediaConstraint).then(stream => {
  // 웹캠의 비디오 스트림을 비디오 요소에 추가
  localStream = stream
  addVideoStream(localVideo, stream)
})


function frontCamera() {
  navigator.mediaDevices.getUserMedia(defaultMediaConstraint).then(stream => {
    // 웹캠의 비디오 스트림을 비디오 요소에 추가
    addVideoStream(remoteVideo, stream)
  })
}

function backCamera() {
  navigator.mediaDevices.getUserMedia(backCameraMediaConstraint).then(stream => {
    // 웹캠의 비디오 스트림을 비디오 요소에 추가
    remoteVideo.msHorizontalMirror = true
    addVideoStream(remoteVideo, stream)
   // remoteVideo.play()
  })
}

function addVideoStream(video, stream) {
  video.srcObject = stream
  video.addEventListener('loadedmetadata', () => {
    video.play()
  })
  // videoGrid.append(video)

}