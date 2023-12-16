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
    let userEmail = $('#userEmail').val();
    let code = $('#code').val();

    if (!userAccount || !userName || !userPwd || !userTel || !userAge || !userEmail || !code) {
        layer.msg("请完整填写信息");
        return;
    }

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
            userEmail: userEmail,
            code: code,
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

/**
 * 发送消息
 */
function send() {
    let message = $('#message').val();
    if (!message) {
        return;
    }
    $('#messages').append("<div class='msg-received msg-sent' style=\"margin-right: 20px\"><div class='msg-content'><p>现在</p><p class='msg'>" + message + "</p></div></div>");
    messageInit();
    $('#message').val('');
    $.ajax({
        type: "POST",
        url: "message/query",
        data: {
            content: message,
        },
        dataType: "json",
        success: function (data) {
            if (data['code'] === 'SUCCESS') {
                message = data['message'];
                $('#messages').append("<div class=\"msg-received\">\n" +
                    "                   <div class=\"msg-image\">\n" +
                    "                      <img src=\"assets/images/team/user-2.jpg\" alt=\"image\">\n" +
                    "                   </div>\n" +
                    "                   <div class=\"msg-content\">\n" +
                    "                      <p>现在</p>\n" +
                    "                      <p class=\"msg\">\n" + message +
                    "                      </p>\n" +
                    "                   </div>\n" +
                    "                  </div>");
                messageInit();
            }
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

