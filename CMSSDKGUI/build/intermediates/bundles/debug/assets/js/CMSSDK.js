(function($) {
	function timeDiff(time){
		var diffdate = new Date().getTime() - time;
		var days = Math.floor(diffdate/(24*3600*1000));
		var leave1 = diffdate%(24*3600*1000);   
		var hours = Math.floor(leave1/(3600*1000));  

		var leave2 = leave1%(3600*1000);
		var minutes = Math.floor(leave2/(60*1000));
		var leave3 = leave2%(60*1000);
		var seconds = Math.round(leave3/1000);
		return (days && days > 0) ? days + "天前" : (hours && hours > 0) ? hours + "小时前" : (minutes && minutes > 0) ? minutes + "分钟前" : "刚刚";
	}
	/**
	 * 获取新闻详情
	 *
	 * @return 返回一个新闻详情对象，如：
	 * {
	 * 	appkey=d580ad56b4b5,
	 * 	displayImgs=[
	 * 		{
	 * 			order=0,
	 * 			url=http: //p3.pstatp.com/list/300x196/18a30005ac5879cc4177.webp
	 * 		},
	 * 		{
	 * 			order=0,
	 * 			url=http: //p2.pstatp.com/list/300x196/18a200059c3adc23f6c6.webp
	 * 		},
	 * 		{
	 * 			order=0,
	 * 			url=http: //p2.pstatp.com/list/300x196/1780001277046345ec81.webp
	 * 		}
	 * 	],
	 * 	articleType=1,
	 * 	categoryIds=[
	 * 		58c65e033fefdff7a1f6d842,
	 * 		58c65e353fefdff7a1f6d84b
	 * 	],
	 * 	content=有当红年轻演员收入超过中型企业balabalabala,
	 * 	displayType=4,
	 * 	readTimes=0,
	 * 	imgSize=0,
	 * 	title=有当红年轻演员收入超过中型企业,
	 * 	id=58c670ae777f7ff978065642,
	 * 	updateAt=1489401811739,
	 * 	hot=1,
	 * 	top=1,
	 * 	comment=0,
	 * 	tags=[
	 * 		综艺,
	 * 		陈维亚,
	 * 		王兴东,
	 * 		真人秀
	 * 	],
	 * 	praiseTimes=0
	 * }
	 */
	function getNewsDetails(callback) {
        try {
            if (window.javaHandler) {
                data = JSON.parse(window.javaHandler.getNewsDetails());
            } else {
                data = JSON.parse(prompt("_$#getNewsDetails#$_"));
            }
        } catch(err) {
        }
        callback(data);
	}
	
	/**
	 * 获取指定位置和数量的评论
	 * 
	 * @param offset 起始位置，从0开始
	 * @param size 最大返回的条数
	 * @return 返回一个评论列表，如：
     * {
     *     "comments":[
     *         {
     *             "id":"58b59a2b733d21f7d9249fe0",
     *             "articleId":"58b58ec33f7dff7adc0bd648",
     *             "avatar":"http://f1.sdk.mob.com/img/2017/01/06/56625ec088bfb88912",
     *             "nickName":"测试",
     *             "content":"评论测试2",
     *             "updateAt":1488515380942
     *         },
     *         {
     *             "id":"58b59a2b733d21f7d9249fe1",
     *             "articleId":"58b58ec33f7dff7adc0bd648",
     *             "avatar":"http://f1.sdk.mob.com/img/2017/01/06/56625ec088bfb88912",
     *             "nickName":"测试",
     *             "content":"评论测试2"
     *             "updateAt":1488515380942
     *         }
     *     ]
     * }
	 */
	function getComments(offset, size, callback) {
        try {
            if (window.javaHandler) {
                data = JSON.parse(window.javaHandler.getComments(offset, size));
            } else {
				var map = {
					offset: offset,
					size: size
				};
                data = JSON.parse(prompt("_$#getComments#$_", JSON.stringify(map)));
            }
        } catch(err) {
        }
        callback(data);
	}
	
	/**
	 * 发起称赞新闻的操作
	 * 
	 * @return 返回是否完成称赞，如：
	 * {
	 * 	"res":true
	 * }
	 */
	function likeNews(callback) {
		var data = null;
        try {
            if (window.javaHandler) {
                data = JSON.parse(window.javaHandler.likeNews());
            } else {
                data = JSON.parse(prompt("_$#likeNews#$_"));
            }
        } catch(err) {
        }
        if(data.res){
            callback(data);
        }
	}


	/** 评论成功回调 */
	function addComment(comment){
		var data = comment;
		data.avatar = (data.avatar && data.avatar != "")  ? data.avatar : "./img/default_user.png";
		var html = '<li class="animated fadeInLeft"><div class="header"><span class="headimg"><img src="'+ data.avatar +'"></span><span>'+ data.nickName +'</span></div><div class="center">'+ data.content + '</div><div class="bottom"><span>'+ timeDiff(data.updateAt) +'</span></div></li>';
		if($(".common-content ul li").length > 0 ){
			$(".common-content ul li").first().before(html);
		} else {
			$(".common-content ul").append(html);
		}
	}

	/** 获取点赞状态 */
	function isPraised(ispraised){
		if(ispraised){
			$(".zan-content .zan").addClass("over active");
		}else{
			$(".zan-content .zan").addClass("over");
		}
	}

	/* 跳到评论内容 */
	function toCommentposition(){
		var getElOffsetTop = $(".main-content")[0].clientHeight;
		$(".content").scrollTo({toT: getElOffsetTop},100,'swing');
	}
	
    /** 定义js-java的方法映射表 */
    window.CMSSDKNative = {
		getNewsDetails: getNewsDetails,
		getComments: getComments,
		likeNews: likeNews,
		addComment: addComment,
		isPraised: isPraised,
		toCommentposition: toCommentposition
    }
})(Zepto);