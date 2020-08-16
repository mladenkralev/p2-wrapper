var isButtonClicked = false;
var app = new Vue({
   
    el: '#addP2Repsitory',
    data: {
        files: '',
        isButtonClicked: false,
    },

    computed: {
        isDisabled () {
            if(!(this.isButtonClicked)) {
                return true
            } else {
                return false
            }
        }
    },
    methods: {
        createRepository() {
            app.isButtonClicked = true;
            let formData = new FormData();
            
            for( var i = 0; i < this.files.length; i++ ){
                let file = this.files[i];
              
                formData.append('files[' + i + ']', file);
            }

            for (var value of formData.values()) {
                console.log(value); 
            }

            axios.post( 
                '/uploadJars',
                formData,
                {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                }
            ).then(function(){
                console.log('SUCCESS!!');
                app.isButtonClicked = false;
            })
            .catch(function(){
                console.log('FAILURE!!');
                app.isButtonClicked = false;
            });
        },

        onChangedUploadFiles() {
            this.files = this.$refs.files.files;
        }
    }
});