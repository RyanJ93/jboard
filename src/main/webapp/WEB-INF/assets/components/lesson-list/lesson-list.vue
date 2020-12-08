<template>
    <div>
        <ul :class="$style.tabs">
            <li data-ref="active-lessons" :data-active="activeTab === 'active-lessons'" v-on:click="pickTab">Active lessons</li>
            <li data-ref="completed-lessons" :data-active="activeTab === 'completed-lessons'" v-on:click="pickTab">Completed lessons</li>
            <li data-ref="cancelled-lessons" :data-active="activeTab === 'cancelled-lessons'" v-on:click="pickTab">Cancelled lessons</li>
        </ul>
        <div :class="$style.tabsWrapper">
            <div :class="$style.tab" :data-active="activeTab === 'active-lessons'">
                <ul :class="$style.list" v-if="lessons.active.length">
                    <li v-for="lesson in lessons.active" :data-lid="lesson.id">
                        <div :class="$style.info">
                            <p :class="$style.title">{{ lesson.course.title }}</p>
                            <p :class="$style.teacher">{{ lesson.teacher.name + ' ' + lesson.teacher.surname }}</p>
                            <p :class="$style.date">{{ getDayName(lesson.day) + ', ' + lesson.hour + ':00 - ' + ( lesson.hour + 1 ) + ':00' }}</p>
                        </div>
                        <div :class="$style.controls">
                            <a class="button" href="javascript:void(0);" v-on:click="mark">Completed</a>
                            <a class="button" href="javascript:void(0);" v-on:click="remove">Delete</a>
                        </div>
                    </li>
                </ul>
                <p :class="$style.emptyMessage" v-else>No active lesson found</p>
            </div>
            <div :class="$style.tab" :data-active="activeTab === 'completed-lessons'">
                <ul :class="$style.list" v-if="lessons.completed.length">
                    <li v-for="lesson in lessons.completed" :data-lid="lesson.id">
                        <div :class="$style.info">
                            <p :class="$style.title">{{ lesson.course.title }}</p>
                            <p :class="$style.teacher">{{ lesson.teacher.name + ' ' + lesson.teacher.surname }}</p>
                            <p :class="$style.date">{{ getDayName(lesson.day) + ', ' + lesson.hour + ':00 - ' + ( lesson.hour + 1 ) + ':00' }}</p>
                        </div>
                    </li>
                </ul>
                <p :class="$style.emptyMessage" v-else>No completed lesson found</p>
            </div>
            <div :class="$style.tab" :data-active="activeTab === 'cancelled-lessons'">
                <ul :class="$style.list" v-if="lessons.cancelled.length">
                    <li v-for="lesson in lessons.cancelled" :data-lid="lesson.id">
                        <div :class="$style.info">
                            <p :class="$style.title">{{ lesson.course.title }}</p>
                            <p :class="$style.teacher">{{ lesson.teacher.name + ' ' + lesson.teacher.surname }}</p>
                            <p :class="$style.date">{{ getDayName(lesson.day) + ', ' + lesson.hour + ':00 - ' + ( lesson.hour + 1 ) + ':00' }}</p>
                        </div>
                    </li>
                </ul>
                <p :class="$style.emptyMessage" v-else>No cancelled lesson found</p>
            </div>
        </div>
    </div>
</template>
<script src="./lesson-list.js"></script>
<style lang="sass" src="./lesson-list.scss" module></style>
