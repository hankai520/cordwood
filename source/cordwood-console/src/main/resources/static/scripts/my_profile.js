/**
 * 应用初始化。
 * */
requirejs(['main'], function(app) {
  app.loadSidebar();
  
  /**
   * 响应“确定裁剪”事件。
   * */
  requirejs(['bootstrap'], function() {
    $('#confirmCrop').click(function() {
      var croppedData = $('#avatarCropper').cropper('getCroppedCanvas').toDataURL('image/jpeg');
      if (croppedData) {
          $('#currentAvatar').attr('src', croppedData);
          $('#avatarBase64Data').val(croppedData);
      }
      $('#uploadAvatar').modal('hide');
    });
  });
  /**
   * 初始化头像裁剪器。
   * */
  requirejs(['cropper'], function() {
    $('#uploadAvatar').on('shown.bs.modal', function(e) {
      var currentAvatar = $('#currentAvatar').attr('src');
      if (currentAvatar) {
        $('#avatarCropper').cropper('replace', currentAvatar);
        $('#avatarPreview').show();
      }
    });

    $("#avatarFile").change(function() {
      if (this.files && this.files[0]) {
        var reader = new FileReader();
        reader.onload = function(e) {
          $('#avatarCropper').cropper('replace', e.target.result);
          $('#avatarPreview').show();
        }
        reader.readAsDataURL(this.files[0]);
      }
    });

    $('#avatarCropper').cropper({
      aspectRatio: 16 / 12,
      movable: false,
      minContainerWidth: 100,
      zoomable: false,
      minCropBoxWidth: 160,
      minCropBoxHeight: 120,
      crop: function(e) {

      }
    });
  });
});