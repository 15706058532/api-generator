const index = {
    /**
     * 初始请求
     */
    init: function () {
        $.get("api/all", {}, function (data) {
            for (let i = 0; i < data.length; i++) {
                let $clazz = $('<div class="clazz"></div>');
                let clazz = data[i];
                let description = clazz.description;
                let $div = $("<div class='clazz-name'></div>");
                $div.text(description);
                let apiMethodList = clazz.apiMethodList;
                let $urls = $('<ul  class="urls"></ul>');
                for (let j = 0; j < apiMethodList.length; j++) {
                    let apiMethod = apiMethodList[j];
                    let $li = $('<li class="uri-li"></li>');
                    let $http = $('<div class="text-color-white left http"></div>');
                    $http.addClass(apiMethod.http.toLowerCase());
                    $http.text(apiMethod.http);
                    let $uri = $('<div class="left uri"></div>');
                    $uri.text(apiMethod.uri);
                    let $copy = $('<div class="copy right">复制URI</div>');

                    let $description = $('<div class="desc"></div>');
                    $description.addClass(apiMethod.http.toLowerCase() + "-desc");
                    $description.text(apiMethod.description);
                    $li.append($http, $uri, $copy, $description);
                    $urls.append($li)

                }
                $clazz.append($div, $urls);
                $("#list").append($clazz);
            }
        }, "json")
    },
    /**
     * 绑定事件
     */
    binding: function () {
        $(document).on("click", ".uri-li .uri", function (e) {
            if ($(this).parent().next().hasClass("param")) {
                $(this).parent().next().remove();
                return
            }
            let $div = $("<div class='param'></div>");
            let $sheet = $("<div class='sheet'></div>");
            let $req = $("<div class='req'>请求参数</div>");
            let $resp = $("<div class='resp'>响应参数</div><div class='clear'></div>");
            $sheet.append($req, $resp);
            $div.append($sheet);
            $(this).parent().after($div);
            index.findReqParam($req, "req");
        });
        $(document).on("click", ".req", function (e) {
            index.findReqParam($(this), "req");
        });
        $(document).on("click", ".resp", function (e) {
            index.findRespParam($(this), "resp");
        });
        $(document).on("click", ".copy", function (e) {
            //禁用冒泡事件
            e.stopPropagation();
            index.copyText($(this))
        });
        $(document).on("click", ".detail-req", function (e) {
            index.findProperties($(this), "req")
        });
        $(document).on("click", ".detail-resp", function (e) {
            index.findProperties($(this), "resp")
        });
        $(document).on("click", ".retract", function (e) {
            if ($(this).parent().next().next().is(":hidden")) {
                $(this).parent().next().next().show();
                $(this).text("收起 ⇈");
            } else {
                $(this).parent().next().next().hide();
                $(this).text("展开 ⇊");
            }
        });
        $(document).on("click", ".goto", function (e) {
            index.goto($(this))
        });
    },
    /**
     * 请求入参
     * @param target
     * @param type
     */
    findReqParam:

        function (target, type) {
            target = target.parent();
            let http = target.parent().prev().find(".http").text();
            let uri = target.parent().prev().find(".uri").text();
            $.get("api/find/req", {uri: http + uri}, function (data) {
                target.next().remove();
                let $contentt = $("<div class='content'></div>");
                $contentt.addClass("content-" + type)
                let $ul = $("<ul class='format-list'></ul>");
                for (let i = 0; i < data.length; i++) {
                    let param = data[i];
                    let $li = $("<li></li>");
                    let $type = $("<div class='title'><div class='type'>参数形式：</div></div>");
                    let $format = $("<div class='format'></div>");
                    $format.text(param.format);
                    let $retract = $("<div class='right retract'>收起 ⇈</div>");
                    $type.append($format, $retract);
                    $li.append($type, $("<div class='clear'></div>"));
                    let $propertiesUl = $("<ul class='param-ul'></ul>");
                    if (param.lzfApiProperties.length > 0) {
                        let $propertiesLi = $("<li></li>");
                        let $name = $("<div class='req-properties-name'></div>");
                        $name.text("名称");
                        if (param.format === "url") {
                            $name.text("url位置");
                        }
                        let $type = $("<div class='req-properties-type'></div>");
                        $type.text("类型");
                        let $must = $("<div class='req-properties-must'></div>");
                        $must.text("是否必须");
                        let $describe = $("<div class='req-properties-desc'></div>");
                        $describe.text("描述");
                        let $look = $("<div class='req-properties-look'></div>");
                        $look.text("操作");
                        $propertiesLi.append($name, $type, $describe, $must, $look);
                        $propertiesUl.append($propertiesLi)
                    }

                    for (let j = 0; j < param.lzfApiProperties.length; j++) {
                        let lzfApiPropertie = param.lzfApiProperties[j];
                        let $propertiesLi = $("<li></li>");
                        let $name = $("<div class='req-properties-name'></div>");
                        $name.text(lzfApiPropertie.name);
                        if (param.format === "url") {
                            $name.text("{" + lzfApiPropertie.name + "}");
                        }
                        let $type = $("<div class='req-properties-type'></div>");
                        $type.text(lzfApiPropertie.type);
                        let $must = $("<div class='req-properties-must'></div>");
                        $must.text(lzfApiPropertie.must);
                        let $describe = $("<div class='req-properties-desc'></div>");
                        $describe.text(lzfApiPropertie.describe);
                        let $look = $("<div class='req-properties-look'></div>");
                        if (lzfApiPropertie.className !== null) {
                            let $detail = $("<div class='detail detail-req'>查看</div>");
                            $look.append($detail);
                            $detail.attr("value", lzfApiPropertie.className)
                        }
                        $propertiesLi.append($name, $type, $describe, $must, $look);
                        $propertiesUl.append($propertiesLi)
                    }
                    $li.append($propertiesUl, $("<div class='clear'></div>"));
                    $ul.append($li);
                }
                $contentt.append($ul);
                target.after($contentt);
            }, "json");
        }

    ,
    /**
     * 请求出参
     * @param target
     * @param type
     */
    findRespParam: function (target, type) {
        target = target.parent();
        let http = target.parent().prev().find(".http").text();
        let uri = target.parent().prev().find(".uri").text();
        $.get("api/find/resp", {uri: http + uri}, function (data) {
            target.next().remove();
            let $contentt = $("<div class='content'></div>");
            $contentt.addClass("content-" + type)
            let $ul = $("<ul class='format-list'></ul>");
            let $li = $("<li></li>");
            let $type = $("<div><span class='type'>响应形式：</span></div>");
            let $format = $("<span class='format'></span>");
            $format.text(data.type);
            let $retract = $("<div class='right retract'>收起 ⇈</div>");
            $type.append($format, $retract);
            $li.append($type, $("<div class='clear'></div>"));
            let $propertiesUl = $("<ul class='param-ul'></ul>");
            if (data.lzfApiProperties.length > 0) {
                let $propertiesLi = $("<li></li>");
                let $name = $("<div class='resp-properties-name'></div>");
                $name.text("名称");
                let $type = $("<div class='resp-properties-type'></div>");
                $type.text("类型");
                let $describe = $("<div class='resp-properties-desc'></div>");
                $describe.text("描述");
                let $look = $("<div class='resp-properties-look'></div>");
                $look.text("操作");
                $propertiesLi.append($name, $type, $describe, $look);
                $propertiesUl.append($propertiesLi)
            }

            for (let j = 0; j < data.lzfApiProperties.length; j++) {
                let lzfApiPropertie = data.lzfApiProperties[j];
                //name: "serialVersionUID", type: "整数", describe: "serialVersionUID", className: null, must: false
                let $propertiesLi = $("<li></li>");
                let $name = $("<div class='resp-properties-name'></div>");
                $name.text(lzfApiPropertie.name);
                let $type = $("<div class='resp-properties-type'></div>");
                $type.text(lzfApiPropertie.type);
                let $describe = $("<div class='resp-properties-desc'></div>");
                $describe.text(lzfApiPropertie.describe);
                let $look = $("<div class='resp-properties-look'></div>");
                if (lzfApiPropertie.className !== null) {
                    let $detail = $("<div class='detail detail-resp'>查看</div>");
                    $look.append($detail);
                    $detail.attr("value", lzfApiPropertie.className)
                }
                $propertiesLi.append($name, $type, $describe, $look);
                $propertiesUl.append($propertiesLi)
            }
            $li.append($propertiesUl, $("<div class='clear'></div>"));
            $ul.append($li);
            $contentt.append($ul);
            target.after($contentt);
        }, "json");
    },
    copyText: function (target) {
        let copyDOM = target.parent().find(".uri")[0];
        let range = document.createRange();
        range.selectNode(copyDOM);
        window.getSelection().removeAllRanges();
        window.getSelection().addRange(range);
        let successful = document.execCommand('copy');
        if (successful) {
            target.text("复制成功");
            target.addClass("success");
            let ref = setInterval(function () {
                target.text("复制URI");
                target.removeClass("success");
                clearInterval(ref);
            }, 800);
        } else {
            alert("复制失败");
        }
        window.getSelection().removeAllRanges();
    },
    findProperties: function (target, form) {
        let parent = target.parent().parent();
        let name = parent.find("." + form + "-properties-name").text();
        let type = parent.find("." + form + "-properties-type").text();
        let value = target.attr("value");
        $.get("api/find/properties", {className: value}, function (data) {
            target.removeClass("detail-" + form);
            target.addClass("goto");
            let parentType = parent.parent().prev().prev().find(".type").first().text().replace("响应形式：", "").replace("参数形式：", "");
            let parentFormat = parent.parent().prev().prev().find(".format").first().text();
            parentFormat = parentType + parentFormat + " >>&nbsp;";
            parent = parent.parent().parent();
            let $type = $("<div class='dashed'></div>");
            $type.attr("id", parentFormat + name + type);
            let $span = $("<span class='type'></span>");
            $span.html(parentFormat);
            $type.append($span);
            let $format = $("<span class='format'></span>");
            $format.text(name);
            let $retract = $("<div class='right retract'>收起 ⇈</div>");
            $type.append($format, $retract);
            let $propertiesType = $("<span class='format'></span>");
            $propertiesType.text(":" + type);
            $type.append($propertiesType);
            let $propertiesUl = $("<ul class='param-ul'></ul>");
            if (data.length > 0) {
                let $propertiesLi = $("<li></li>");
                let $name = $("<div class='" + form + "-properties-name'></div>");
                $name.text("名称");
                let $type = $("<div class='" + form + "-properties-type'></div>");
                $type.text("类型");
                let $must = $("<div class='" + form + "-properties-must'></div>");
                $must.text("是否必须");
                let $describe = $("<div class='" + form + "-properties-desc'></div>");
                $describe.text("描述");
                let $look = $("<div class='" + form + "-properties-look'></div>");
                $look.text("操作");
                if (form === "req") {
                    $propertiesLi.append($name, $type, $describe, $must, $look);
                } else {
                    $propertiesLi.append($name, $type, $describe, $look);
                }
                $propertiesUl.append($propertiesLi)
            }
            for (let j = 0; j < data.length; j++) {
                let lzfApiPropertie = data[j];
                let $propertiesLi = $("<li></li>");
                let $name = $("<div class='" + form + "-properties-name'></div>");
                $name.text(lzfApiPropertie.name);
                let $type = $("<div class='" + form + "-properties-type'></div>");
                $type.text(lzfApiPropertie.type);
                let $must = $("<div class='" + form + "-properties-must'></div>");
                $must.text(lzfApiPropertie.must);
                let $describe = $("<div class='" + form + "-properties-desc'></div>");
                $describe.text(lzfApiPropertie.describe);
                let $look = $("<div class='" + form + "-properties-look'></div>");
                if (lzfApiPropertie.className !== null) {
                    let $detail = $("<div class='  detail-" + form + "'>查看</div>");
                    $look.append($detail);
                    $detail.attr("value", lzfApiPropertie.className)
                }
                if (form === "req") {
                    $propertiesLi.append($name, $type, $describe, $must, $look);
                } else {
                    $propertiesLi.append($name, $type, $describe, $look);
                }
                $propertiesUl.append($propertiesLi)
            }
            parent.append($("<div class='clear'></div>"), $type, $("<div class='clear'></div>"), $propertiesUl, $("<div class='clear'></div>"));
            location.href = "#" + parentFormat + name + type;
            target.attr("value", parentFormat + name + type)
        }, "json");
    },
    goto: function (target) {
        location.href = "#" + target.attr("value");
    }
};
$(function () {
    index.init();
    index.binding();
});