package com.jov.laughter.bean;

import java.util.ArrayList;
import java.util.List;

public class TextBean {
	private int tid;
	private String imagUrl;
	private String content;
	private String colDate;
	private List<TextBean> list ;
	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public String getImagUrl() {
		return imagUrl;
	}

	public void setImagUrl(String imagUrl) {
		this.imagUrl = imagUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getColDate() {
		return colDate;
	}

	public void setColDate(String colDate) {
		this.colDate = colDate;
	}

	public List<TextBean> initSelf() {
		if(list==null||list.size()==0){
			list = new ArrayList<TextBean>();
			TextBean bean;
			String coldate = "20140920";
			for (String str : locals()) {
				bean = new TextBean();
				bean.setColDate(coldate);
				bean.setContent(str);
				list.add(bean);
			}
		}
		return list;
	}

	private String[] locals() {

		String[] texts = {
				"公司一妹子问我“你们男人到底哪里比我们女的尊贵啊？为什么昨天我住宾馆的时候，一样的房间我交168,而他们男人却交468？” 朋友们，我该怎么跟她解释这多出来的300块钱……",
				"在地铁口碰到一个乞丐，他说只要我给他钱他什么都愿意干。我给了他一张一百的，麻烦他帮我换成散的。",
				"今天遇到件事，一小偷在餐厅偷一顾客东西（具体是偷什么就不清楚了），被发现后想逃跑！那顾客没喊抓小偷，他来了句：抓住他，他还没买单！……服务员一拥而上…",
				"你们有没有这样的体会：每次坐火车都会遇到一个上知天文，下知地理，关系遍地球的中年大叔。",
				"小时候家里穷，爸妈工资一个月几百块钱，当时我就想长大以后能找份月薪2000多的工作该多好，结果愿望实现了……",
				"胖女孩在小学完全没人爱，可一升初中，马上就没人爱了。不过这份痛苦也就持续三年，等到高中，男孩子懂事了，胖女孩们就渐渐地没人爱了。但上天终究是公平的。经历了痛彻心扉的高考，进入伊甸园般的大学后，之前的各种没人爱，都有如泡沫般，消逝地无影无踪。长大成人的胖女孩们，终于还是没人爱。",
				"每天，女孩都会来他店里吃饭，坐靠窗位置，点上两份套餐，每次他问起几位用餐，她总是羞涩说两个人，可后却是一个人默默吃完。他想也许曾经有个人陪在女孩身边，但那人已经不在了。终于有天，他想过去问聆听女孩的故事，却听到那女孩自言自语：＂两份的量也那么少，不够吃啊，艹！＂",
				"老板教育我:“在这个残酷的世界上，机会变得很重要。抓住机会活，抓不住机会死”。我点点头，赶紧去菜市场抓了一只鸡……",
				"今天，我们公司的两个娘炮终于像个爷们一样的干了一架，原因是一个女同事送了他们俩三张进口的美白面膜。。。",
				"不开心的时候，我就会群发一条短信写着「Hi我刚换了一部新手机，里面电话都没了，可以把你的号码发一次给我吗？」然后你就可以发现你朋友里的傻逼们了",
				"刚才看个帅哥，骑个大摩托，轰轰响，大长头发，大墨镜，穿梭在晚上八点的街头。那速度老快了，太帅了！。就是抬上救护车的时候有点难看。",
				"我指了下那块肥皂，没想到那货不为所动。不得已，只得出声提醒道：喂，你肥皂掉了。他这才反应过来，道了声谢弯腰去捡。就在那刻，我以迅雷不及掩耳之势奔袭到他身边，双手搭上他的背，跳了个山羊。哇，太激动了，我这一个星期都在想玩一次跳山羊，终于得偿所愿！",
				"今天相亲总算见到个传说中的女汉子，一起吃的牛排，她说用不惯刀叉，一直愤愤叨叨的嫌麻烦。后来实在忍不住了霸气的来了一句：“服务员！来双筷子！”在服务员解释吃牛排不好用筷子后，她又大声来了句：“一次性手套也行！”今天相亲总算见到个传说中的女汉子，一起吃的牛排，她说用不惯刀叉，一直愤愤叨叨的嫌麻烦。后来实在忍不住了霸气的来了一句：“服务员！来双筷子！”在服务员解释吃牛排不好用筷子后，她又大声来了句：“一次性手套也行！”",
				"有个吃货女朋友真好，别人哄女朋友都要说带你买衣服买首饰买包包......而你只需要说，走，带你吃好吃的去~立马就活蹦乱跳有木有。省钱省力省时间，你值得拥有。。。",
				"我姐和喜欢的男神一起吃夜宵，姐吃很撑，扶着腰上公交，居然被让座了..让座阿姨慈祥地笑着看看姐的肚子，一脸【几个月呀~】..姐惊恐地站起来，此时一直偏着脸笑的男神把她按回去说“没事，你坐啦”阿姨一脸yo地拍肩说你老公挺疼你的嘛...下了车男神跟姐告白了，还摸着姐的肚子说给我生个娃呗。",
				"中午和老婆去看了套房子…我感觉挺不错…价钱合适…升值空间大…问老婆的意见…老婆憋了半会…弱弱地来了句：挺好的…就是…就是…隔壁屋女主人凉的内衣太情趣了…我得考虑考虑…",
				"女朋友很喜欢吃一种叫“骨肉相连”的烤串，今天与女友外出就餐，又点了骨肉相连。一会儿，一小弟拿着盘子走了过来，放桌子上，并且热心地说：“你们的骨肉。”尼玛，说话能不那么简洁吗！",
				"刚刚看到了一个小故事好感动，说是一对老夫妻吃鸡蛋，其中一方喜欢吃蛋黄就把蛋黄给对方自己吃蛋青，最后才知道原来对方喜欢吃蛋青……这让我突然想起了我和妈妈，我和妈妈吃鸡蛋也是这样，她总是会把鸡蛋吃掉把蛋壳留给我。",
				"今天在路上看到有人模仿MJ的太空步,虽然他表演得很卖力,但舞姿实在是不堪入目,我忍不住上前拍了拍他的肩膀说：“小伙子,回家练练再出来演吧,你这样简直是对杰克逊的侮辱。”他停下来,一脸错愕地对我说：“杰克逊是谁？我只是刚才不小心踩到别人嚼过的口香糖了…”",
				"昨天深夜，一位2b朋友打电话给我，说他车里的方向盘、刹车、油门、离合全被人卸掉了，叫我去接他。我开车到半路的时候，这货又打电话给我说：你不用过来了，刚才喝多了，现在清醒一点才发现原来自己坐在副驾驶座上。。",
				"同学抄作业，自作聪明把“一周”改成“一星期”，至今忘不了物理老师看到“小球旋转一星期”时那扭曲的脸。。。",
				"在学校宿舍楼里半夜12点，夜深人静，四楼某脑残人士突然传出一声大吼：我家住在黄土高坡，全楼无语中。。。 然后三楼某窗口悠悠的飘出一句：你爸是你妈表哥……全楼沸腾了。",
				"昨晚我在回家的路上的时候前面有个女的，我们都走得很慢，保持距离大概两米，那时整条街都没什么人了，很安静…大概走了十几分钟，那女的脚步越来越慢，忽然她回过头对我说：“你再不下手我就到家了”。",
				"交车上一年轻的妈妈给宝宝喂奶，宝宝吃得不老实，年轻的妈妈生气说孩子：“吃不吃？不吃我给旁边的叔叔吃了 ”一连说了几次。坐旁边的叔叔忍不住说：“我的小少爷，吃不吃给个准信，叔叔都坐超两站了” ",
				"记者采访一位老奶奶！记者问：“你对在城市随便燃放鞭炮这个问题您怎么看啊？”老奶奶：“我还能怎么看啊？就是爬窗户上看……”",
				"老公老婆在一个被子里睡觉，老公打了一个喷嚏，喷了老婆一脸。老婆说：再有情况时提前说一声，过了一会。老公大声说：预备……老婆赶忙一头钻进被子里，只听“嘭”的一声，老公放了一个屁。",
				"有对夫妻，郎才女貌，生了个小孩快两岁了，却总觉得小孩跟夫妻俩长的都不像。夫妻俩越来越不安，怀疑是不是当初在医院分娩时被医院给弄错了，于是一家三口去做了DNA亲子鉴定。鉴定的结果：丈夫跟小孩有血缘关系，妻子没有。",
				"爷爷告诉我，说他娶奶奶的时候只用了“半斗米”；爸爸告诉我，说他娶妈妈时总共用了“半头猪”；等我要结婚的时候，用了我爹妈“半条命”。 ",
				"一天，一理发师把一个卖糖葫芦的揍了，到警察局警察问理发师：你为什么揍卖糖葫芦的？理发师说：他妈的，我在屋里烫头发，他在外面喊“烫糊喽，烫糊喽”",
				"老板刚进办公室，部门经理的老婆闯了进来，挥着一条女式三角裤对老板说：“我老公晚上回家竟然穿了女人的内裤，您一定要管管”。老板连连点头是是，顺手把三角裤塞进口袋里。晚上回家，老板老婆洗衣服时发现了老板口袋里的三角裤，对老板说：“以后不许开这样玩笑了，害的人家找了一整天”。",
				"有次7岁的小侄女非要和我一起洗澡，边洗还边说：“姑姑，你的胸为什么那么小？”我狂汗：“哪小了，怎么小了？”小侄女可怜地看了我一眼，安慰道：“没事，我的也很小”",
				"一哥们思想斗争许久，终鼓起勇气在QQ上向MM深情表白，心里打着小鼓等待答复。不一会儿MM回复：“我是她的妈妈，我是上来偷菜的”" };
		return texts;
	}
}
