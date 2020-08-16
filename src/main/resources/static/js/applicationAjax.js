// $(document).ready(function () {
//     var fileSelect = document.getElementById('files-select-repository');
//     var formData = new FormData();
    
//     $("#upload-button-repository").click(function (event) {

//         // event.preventDefault();

//         var files = fileSelect.files;

//         jQuery.each(files, function (i, file) {
//             formData.append('file[' + i + ']', file);
//         });

//         // Display the values
//         for (var value of formData.values()) {
//             console.log(value);

//         }
//         $.ajax({
//             type: 'POST',
//             url: '/uploadJars/',
//             data: formData,
//             contentType: false,
//             processData: false,
//             cache: false,
//             enctype: "multipart/form-data",
//             method: 'POST',
//             type: 'POST', // For jQuery < 1.9
//             // beforeSend: function(xhr, settings) {
//             //     xhr.setRequestHeader("Content-Type", "multipart/form-data;boundary=gc0p4Jq0M2Yt08jU534c0p");
//             //     settings.data = {name: "file", file: inputElement.files[0]};                    
//             // },
//             beforeSend: function() {
//                 $('#addP2RepositoryModal').empty();
//                 $('#loading').html("<img id='loading-img' src='./images/Drops-128px.gif' />");
//             },
//             success: function (value) {
//                 console.log('Success: ' + value);
//                 $("#customFile").val(''); 
//                 // $('#loading').empty();
//             },
//             error: function (error) {
//                 $("#customFile").val(''); 
//                 console.log('Error: ' + error);
//                 // $('#loading').empty();
//             }
//         });
//     });
// });

// $(document).ready(function () {
//     $("#refresh-repository-button").click(function (event) {
//         console.log("rdy");
//         $.ajax({
//             type: 'GET',
//             url: 'getRepositories/',
//             contentType: false,
//             processData: false,
//             cache: false,
//             method: 'GET',
//             type: 'GET', 
//             success: function (value) {
//                 console.log('Success: ' + value);
//                 value.forEach(function(obj) { 
//                     console.log(obj); 
//                     var $input = $(
//                         '<div class="row">' + 
//                             '<div class="col-6">'+
//                                 '<input type="button" value="'+ obj +'" class="rounded-circle" />'+ 
//                             '</div>'+ 
//                             '<div class="col-6">'+
//                             '</div>'+
//                         '</div>');
//                     $input.appendTo($("#test-container"));
//                 });
//             },
//             error: function (error) {
//                 console.log('Error: ' + error);
//                 var $input = $(
//                     '<div class="row">' + 
//                         '<div class="col-6">'+
//                             '<input type="button" value="'+ error +'" class="rounded-circle" />'+ 
//                         '</div>'+ 
//                         '<div class="col-6">'+
//                         '</div>'+
//                     '</div>');
//                 $input.appendTo($("#test-container"));
//             }
//         });
//     });

// });
