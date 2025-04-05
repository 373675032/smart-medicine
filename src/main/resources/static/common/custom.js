/**
 * 发送验证码
 */
function sendEmailCode() {
    let email = $('#userEmail').val();
    if (!email) {
        layer.msg("邮箱不能为空");
        return;
    }
    if (!emailReg(email)) {
        layer.msg("请输入正确的邮箱地址");
        return;
    }
    $.ajax({
        type: "POST",
        url: "login/sendEmailCode",
        data: {
            email: email,
        },
        dataType: "json",
        success: function (data) {
            if (data['code'] !== 'SUCCESS') {
                layer.msg(data['message'])
            } else {
                // 禁用按钮，60秒倒计时
                time("#email-code", 60);
            }
        }
    });
}

/**
 * 注册
 */
function register() {
    let userAccount = $('#userAccount').val();
    let userName = $('#userName').val();
    let userPwd = $('#userPwd').val();
    let userTel = $('#userTel').val();
    let userAge = $('#userAge').val();
    let userSex = $('#userSex').find("option:selected").val();
    let userType = $('#userType').find("option:selected").val();
    let userEmail = $('#userEmail').val();
    let code = $('#code').val();

    if (!userAccount || !userName || !userPwd || !userTel || !userAge || !userEmail || !code) {
        layer.msg("请完整填写信息");
        return;
    }
    
    // 如果是医生，检查资质图片
    if (userType === "1" && $('#qualification-files')[0].files.length === 0) {
        layer.msg("请上传医生资质证明");
        return;
    }
    
    // 显示加载中
    const loadingIndex = layer.load(1, {
        shade: [0.1, '#fff']
    });
    
    // 处理逻辑：先上传图片，再提交表单
    const registerUser = (qualificationImages) => {
        $.ajax({
            type: "POST",
            url: "login/register",
            data: {
                userAccount: userAccount,
                userName: userName,
                userPwd: userPwd,
                userTel: userTel,
                userAge: userAge,
                userSex: userSex,
                userType: userType,
                userEmail: userEmail,
                code: code,
                doctorQualificationImages: qualificationImages
            },
            dataType: "json",
            success: function (data) {
                layer.close(loadingIndex);
                layer.msg(data['message']);
                if (data['code'] === 'SUCCESS') {
                    setTimeout('reload()', 2000);
                }
            },
            error: function() {
                layer.close(loadingIndex);
                layer.msg('注册失败，请稍后重试');
            }
        });
    };
    
    // 根据用户类型，决定是否需要上传资质图片
    if (userType === "1") {
        uploadQualificationImages()
            .then(imagePaths => {
                registerUser(imagePaths);
            })
            .catch(() => {
                layer.close(loadingIndex);
            });
    } else {
        registerUser('');
    }
}

/**
 * 登录
 */
function login() {
    let loginAccount = $('#loginAccount').val();
    let loginPassword = $('#loginPassword').val();
    if (!loginAccount || !loginPassword) {
        layer.msg("请完整登录信息");
        return;
    }
    $.ajax({
        type: "POST",
        url: "login/login",
        data: {
            userAccount: loginAccount,
            userPwd: loginPassword
        },
        dataType: "json",
        success: function (data) {
            layer.msg(data['message']);
            if (data['code'] === 'SUCCESS') {
                setTimeout('reload()', 2000);
            }
        }
    });

}

/**
 * 修改资料
 */
function updateProfile() {
    let id = $('#userId').val();
    let userName = $('#userName').val();
    let userTel = $('#userTel').val();
    let userAge = $('#userAge').val();
    let imgPath = $('#img').val();

    if (!userName || !userTel || !userAge) {
        layer.msg("请完整填写信息");
        return;
    }

    $.ajax({
        type: "POST",
        url: "user/saveProfile",
        data: {
            id: id,
            userName: userName,
            userTel: userTel,
            userAge: userAge,
            imgPath: imgPath,
        },
        dataType: "json",
        success: function (data) {
            layer.msg(data['message']);
            if (data['code'] === 'SUCCESS') {
                setTimeout('reload()', 2000);
            }
        }
    });
}

/**
 * 上传头像
 */
function uploadPhoto() {
    var formdata = new FormData();
    formdata.append("file", $("#img-file").get(0).files[0]);
    $.ajax({
        async: false,
        type: "POST",
        url: "file/upload",
        dataType: "json",
        data: formdata,
        contentType: false,//ajax上传图片需要添加
        processData: false,//ajax上传图片需要添加
        success: function (data) {
            console.log(data);
            layer.msg(data['message']);
            $("#img-preview").attr('src', data['data']);
            $("#img").attr('value', data['data']);
        }
    });
}

/**
 * 修改资料
 */
function updatePassword() {
    let oldPass = $('#oldPassword').val();
    let password1 = $('#password1').val();
    let password2 = $('#password2').val();

    if (!oldPass || !password1 || !password2) {
        layer.msg("请完整填写信息");
        return;
    }

    if (password1 !== password2) {
        layer.msg("两次密码不一致");
        return;
    }

    $.ajax({
        type: "POST",
        url: "user/savePassword",
        data: {
            oldPass: oldPass,
            newPass: password1,
        },
        dataType: "json",
        success: function (data) {
            layer.msg(data['message']);
            if (data['code'] === 'SUCCESS') {
                setTimeout('reload()', 1000);
            }
        }
    });
}

/**
 * 60秒倒计时
 * @param o
 */
function time(o, wait) {
    if (wait === 0) {
        $(o).attr("disabled", false);
        $(o).html("获取");
    } else {
        $(o).attr("disabled", true);
        $(o).html(wait + "秒");
        wait--;
        setTimeout(function () {
            time(o, wait);
        }, 1000);
    }
}

/**
 * 刷新页面
 */
function reload() {
    window.location.reload();
}

/**
 * 跳转到指定页面
 * @param url
 */
function reloadTo(url) {
    let appCnName = APPLICATION_EN_NAME();
    let href = window.location.href;
    href = href.split("/" + appCnName)[0] + "/" + appCnName + url;
    window.location.href = href;
}

/**
 * 跳转到指定页面
 * @param url
 */
function reloadToGO(url) {
    window.location.href = url;
}

/**
 * 分享网站
 */
function share() {
    alert("请复制链接后分享：" + window.location.href);
}

/**
 * 提交反馈
 */
function feedback() {
    let name = $('#name').val();
    let email = $('#email').val();
    let title = $('#title').val();
    let content = $('#content').val();

    if (!name || !email || !title || !content) {
        layer.msg("请完整填写信息");
        return;
    }

    $.ajax({
        type: "POST",
        url: "feedback/save",
        data: {
            name: name,
            email: email,
            title: title,
            content: content,
        },
        dataType: "json",
        success: function (data) {
            layer.msg(data['message']);
            if (data['code'] === 'SUCCESS') {
                setTimeout('layer.msg(\'我们已经收到了你的反馈，感谢您的支持！\');', 2000);
                setTimeout('reload()', 4000);
            }
        }
    });
}

/**
 * 保存疾病
 */
function saveIllness() {
    let id = $('#id').val();
    let illnessName = $('#illnessName').val();
    let includeReason = $('#includeReason').val();
    let illnessSymptom = $('#illnessSymptom').val();
    let specialSymptom = $('#specialSymptom').val();
    let kindId = $('#kindId').find("option:selected").val();

    $.ajax({
        type: "POST",
        url: "illness/save",
        data: {
            id: id,
            illnessName: illnessName,
            includeReason: includeReason,
            illnessSymptom: illnessSymptom,
            specialSymptom: specialSymptom,
            kindId: kindId,
        },
        dataType: "json",
        success: function (data) {
            layer.msg(data['message']);
            if (data['code'] === 'SUCCESS') {
                setTimeout('reload()', 2000);
            }
        }
    });
}

/**
 * 保存药品
 */
function saveMedicine() {
    let id = $('#id').val();
    let imgPath = $('#img').val();
    let medicineName = $('#medicineName').val();
    let keyword = $('#keyword').val();
    let medicineBrand = $('#medicineBrand').val();
    let medicinePrice = $('#medicinePrice').val();
    let medicineEffect = $('#medicineEffect').val();
    let interaction = $('#interaction').val();
    let taboo = $('#taboo').val();
    let usAge = $('#usAge').val();
    let medicineType = $('#medicineType').find("option:selected").val();

    $.ajax({
        type: "POST",
        url: "medicine/save",
        data: {
            id: id,
            imgPath: imgPath,
            medicineName: medicineName,
            keyword: keyword,
            medicineBrand: medicineBrand,
            medicinePrice: medicinePrice,
            medicineEffect: medicineEffect,
            interaction: interaction,
            taboo: taboo,
            usAge: usAge,
            medicineType: medicineType,
        },
        dataType: "json",
        success: function (data) {
            layer.msg(data['message']);
            if (data['code'] === 'SUCCESS') {
                setTimeout('reload()', 2000);
            }
        }
    });
}

/**
 * 删除疾病
 * @param id
 */
function deleteIllness(id) {
    $.ajax({
        type: "POST",
        url: "illness/delete",
        data: {
            id: id,
        },
        dataType: "json",
        success: function (data) {
            layer.msg(data['message']);
            if (data['code'] === 'SUCCESS') {
                setTimeout('reload()', 2000);
            }
        }
    });
}

/**
 * 删除药品
 * @param id
 */
function deleteMedicine(id) {
    $.ajax({
        type: "POST",
        url: "medicine/delete",
        data: {
            id: id,
        },
        dataType: "json",
        success: function (data) {
            layer.msg(data['message']);
            if (data['code'] === 'SUCCESS') {
                setTimeout('reload()', 2000);
            }
        }
    });
}

/**
 * 保存关系
 * @param illnessId
 * @param medicineId
 */
function saveIllnessMedicine(illnessId, medicineId) {
    $.ajax({
        type: "POST",
        url: "illness_medicine/save",
        data: {
            illnessId: illnessId,
            medicineId: medicineId,
        },
        dataType: "json",
        success: function (data) {
            layer.msg('修改成功');
        }
    });
}

/**
 * 删除关系
 * @param id
 */
function deleteIllnessMedicine(id) {
    $.ajax({
        type: "POST",
        url: "illness_medicine/delete",
        data: {
            id: id,
        },
        dataType: "json",
        success: function (data) {
            layer.msg('修改成功');
        }
    });
}

/**
 * 删除反馈
 * @param id
 */
function deleteFeedback(id) {
    $.ajax({
        type: "POST",
        url: "feedback/delete",
        data: {
            id: id,
        },
        dataType: "json",
        success: function (data) {
            layer.msg(data['message']);
            if (data['code'] === 'SUCCESS') {
                setTimeout('reload()', 2000);
            }
        }
    });
}

/**
 * 初始化聊天窗口滚动条
 */
function messageInit() {
    let height = $("#messages")[0].scrollHeight;
    $("#messages").scrollTop(height);
}

// 添加一个全局变量来存储当前上传的图片URL
let currentImageUrls = [];

// 添加图片预览功能
$('#report-files').on('change', async function(e) {
    const files = e.target.files;
    if (files.length === 0) return;
    
    // 创建FormData对象
    const formData = new FormData();
    for (let i = 0; i < files.length; i++) {
        formData.append("files", files[i]);
    }
    
    try {
        // 上传文件
        const response = await $.ajax({
            type: "POST",
            url: "file/uploadMultiple",
            data: formData,
            processData: false,
            contentType: false
        });
        
        if (response.code === 'SUCCESS') {
            // 清空预览区域
            $('#image-preview').empty();
            // 清空之前的URLs
            currentImageUrls = [];
            
            // 显示预览图并存储URLs
            response.data.forEach(url => {
                currentImageUrls.push(url);
                $('#image-preview').append(`
                    <div class="position-relative" style="width: 60px; height: 60px;">
                        <img src="${url}" style="width: 100%; height: 100%; object-fit: cover; border-radius: 4px;">
                        <button onclick="removeImage('${url}')" class="position-absolute" 
                                style="top: -5px; right: -5px; border: none; background: red; color: white; 
                                border-radius: 50%; width: 20px; height: 20px; padding: 0; line-height: 1;">
                            ×
                        </button>
                    </div>
                `);
            });
        } else {
            layer.msg('上传图片失败：' + response.message);
        }
    } catch (error) {
        layer.msg('上传图片失败');
        console.error(error);
    }
});

// 添加删除图片功能
function removeImage(url) {
    currentImageUrls = currentImageUrls.filter(i => i !== url);
    $('#image-preview').find(`img[src="${url}"]`).parent().remove();
}

/**
 * 发送消息
 */
function send() {
    let message = $('#message').val();
    if (!message && currentImageUrls.length === 0) {
        layer.msg('请输入消息或上传图片');
        return;
    }

    // 显示用户发送的消息
    let userMessageHtml = "<div class='msg-received msg-sent' style=\"margin-right: 20px\">";
    userMessageHtml += "<div class='msg-content'><p>现在</p>";
    userMessageHtml += "<p class='msg'>" + message + "</p>";
    
    // 显示发送的图片
    if (currentImageUrls.length > 0) {
        userMessageHtml += "<div class='d-flex flex-wrap' style='gap: 10px; margin-top: 10px;'>";
        currentImageUrls.forEach(url => {
            userMessageHtml += `<img src="${url}" style="max-width: 200px; max-height: 200px; object-fit: cover; border-radius: 4px;">`;
        });
        userMessageHtml += "</div>";
    }
    
    userMessageHtml += "</div></div>";
    $('#messages').append(userMessageHtml);
    messageInit();
    $('#message').val('');

    // 获取当前页面的协议、域名和端口
    const baseUrl = window.location.protocol + '//' + window.location.host;
    // 转换图片URL为完整路径
    const fullImageUrls = currentImageUrls.map(url => url.startsWith('http') ? url : baseUrl + url);

    $.ajax({
        type: "POST",
        url: "message/query",
        contentType: "application/json",
        data: JSON.stringify({
            content: message || "请帮我分析这些医疗报告图片",
            image_urls: fullImageUrls  // 使用转换后的完整URL
        }),
        dataType: "json",
        success: function (data) {
            if (data['code'] === 'SUCCESS') {
                let responseMessage = data['message'];
                $('#messages').append(`
                    <div class="msg-received">
                        <div class="msg-image">
                            <img src="${baseUrl}/assets/images/team/user-2.jpg" alt="image">
                        </div>
                        <div class="msg-content">
                            <p>现在</p>
                            <p class="msg">${responseMessage}</p>
                        </div>
                    </div>
                `);
                messageInit();
                
                // 清空当前图片列表和预览
                currentImageUrls = [];
                $('#image-preview').empty();
            } else {
                layer.msg('发送失败：' + data['message']);
            }
        },
        error: function(xhr, status, error) {
            layer.msg('请求失败：' + error);
        }
    });
}

/**
 * 搜索病
 */
function searchGroup(kind) {
    let content = $("#search").val().trim();
    if (content == ""){
        xtip.msg("请输入查询内容");
        return;
    }
    let href = window.location.href;
    href = href.split("/")[0] + "/findIllness?illnessName="+content+"&kind="+kind;
    reloadToGO(href);
}

/**
 * 搜索病
 */
function searchGroupByName() {
    let content = $("#search").val().trim();
    if (content == ""){
        xtip.msg("请输入查询内容");
        return;
    }
    let href = window.location.href;
    href = href.split("/")[0] + "/findIllness?illnessName="+content;
    reloadToGO(href);
}

/**
 * 搜索一下
 */
function searchGlobalSelect() {
    let content = $("#cf-search-form").val().trim();
    if (content == ""){
        xtip.msg("请输入查询内容");
        return;
    }
    let href = window.location.href;
    alert(href);
    href = href.split("/")[0] + "/findIllness?illnessName="+content;
    reloadToGO(href);
}
/**
 * 搜索药
 */
function searchMedicine() {
    let content = $("#search-medicine").val().trim();
    if (content == ""){
        xtip.msg("请输入查询内容");
        return;
    }
    let href = window.location.href;
    href = href.split("/")[0] + "/findMedicines?nameValue="+content;
    reloadToGO(href);
}

/**
 * 切换医生资质上传区域显示/隐藏
 */
function toggleQualificationUpload() {
    let userType = $('#userType').val();
    if (userType === "1") {
        $('#qualificationUploadArea').slideDown();
    } else {
        $('#qualificationUploadArea').slideUp();
    }
}

/**
 * 预览选择的资质图片
 */
function previewQualificationImages(input) {
    const maxFiles = 5;
    const files = input.files;
    
    // 检查文件数量限制
    if (files.length > maxFiles) {
        layer.msg(`最多只能上传${maxFiles}张图片`);
        input.value = '';
        previewArea.empty();
        return;
    }
    
    // 清空预览区域
    const previewArea = $('#qualification-previews');
    previewArea.empty();
    
    // 预览每张图片
    for (let i = 0; i < files.length; i++) {
        let file = files[i];
        
        // 检查文件类型
        if (!file.type.startsWith('image/')) {
            layer.msg('请上传图片文件');
            input.value = '';
            previewArea.empty();
            return;
        }
        
        // 创建预览元素
        let reader = new FileReader();
        reader.onload = function(e) {
            let preview = `
                <div class="position-relative" style="width: 100px; height: 100px;">
                    <img src="${e.target.result}" class="img-thumbnail" style="width: 100%; height: 100%; object-fit: cover;">
                    <button type="button" class="btn btn-sm btn-danger position-absolute" style="top: 0; right: 0; padding: 0 5px;" onclick="removePreview(this, ${i})">
                        <i class="fa fa-times"></i>
                    </button>
                </div>
            `;
            previewArea.append(preview);
        }
        reader.readAsDataURL(file);
    }
}

/**
 * 移除预览图片
 */
function removePreview(button, index) {
    // 移除预览图片
    $(button).parent().remove();
    
    // 从input中移除文件
    // 注意：由于无法直接修改FileList，这里使用一个技巧：创建新的 DataTransfer 对象
    const fileInput = document.getElementById('qualification-files');
    const dt = new DataTransfer();
    
    const files = fileInput.files;
    for (let i = 0; i < files.length; i++) {
        if (i !== index) {
            dt.items.add(files[i]);
        }
    }
    
    fileInput.files = dt.files;
}

/**
 * 上传医生资质图片
 * @returns {Promise} 包含上传图片路径的Promise
 */
function uploadQualificationImages() {
    return new Promise((resolve, reject) => {
        const files = $('#qualification-files')[0].files;
        if (files.length === 0) {
            resolve('');
            return;
        }
        
        // 创建FormData对象
        const formData = new FormData();
        for (let i = 0; i < files.length; i++) {
            formData.append("files", files[i]);
        }
        
        // 上传文件
        $.ajax({
            type: "POST",
            url: "file/uploadMultiple",
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                if (response.code === 'SUCCESS') {
                    // 图片路径以逗号分隔
                    const imagePaths = response.data.join(',');
                    $('#doctorQualificationImages').val(imagePaths);
                    resolve(imagePaths);
                } else {
                    layer.msg(response.message);
                    reject(response.message);
                }
            },
            error: function(error) {
                layer.msg('上传图片失败');
                reject(error);
            }
        });
    });
}

// 假设你需要一个方法来添加图片URL
function addImageUrl(url) {
    if (!window.imageUrls) {
        window.imageUrls = [];
    }
    window.imageUrls.push(url);
}

// 假设你需要一个方法来清除图片URL列表
function clearImageUrls() {
    window.imageUrls = [];
}

